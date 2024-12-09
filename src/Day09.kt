import kotlin.time.measureTime

class Day09 {

    companion object {
        const val TEST1 = 1928L
        const val TEST2 = 2858L

        fun parseInput(input: String): Pair<List<Int?>, Int> {
            var fileQuantity = 0
            return buildList {
                input.forEachIndexed { index, ch ->
                    val isFile = index % 2 == 0
                    val addVal = if (isFile) index / 2 else null
                    if (isFile) fileQuantity += ch.digitToInt()
                    repeat(ch.digitToInt()) {
                        add(addVal)
                    }
                }
            } to fileQuantity
        }

        fun List<Int?>.checksum(): Long = this.mapIndexed { index, ch -> (ch ?: 0) * index.toLong() }.sum()

        fun List<Int?>.debug(): List<Int?> {
            println(this.joinToString("") { it?.toString() ?: "." })
            return this
        }

        fun part1(input: String): Long {
            val (initialMemory, fileQuantity) = parseInput(input)
            val finalMemory = initialMemory.subList(0, fileQuantity).toMutableList()
            initialMemory.subList(fileQuantity, initialMemory.size).filterNotNull().reversed().forEach { fileId ->
                val indexOfNull = finalMemory.indexOfFirst { ch -> ch == null }
                finalMemory[indexOfNull] = fileId
            }

            return finalMemory.checksum()
        }

        fun part2(input: String): Long {
            val (initialMemory, _) = parseInput(input)
            val (segMemory, _) = initialMemory.fold(mutableListOf<MutableList<Int?>>() to null as Int?) { (acc, prev), value ->
                if (value == prev) {
                    acc.last().add(value)
                } else {
                    acc.add(mutableListOf(value))
                }
                acc to value
            }

            segMemory.reversed().filter { it.first() != null }.forEach { memory ->
                val indexToReplace = segMemory.indexOfFirst { it.size >= memory.size && it.first() == null }
                val regularIndex = segMemory.indexOfFirst { memToRemove -> memToRemove.first() == memory.first() }
                if (indexToReplace != -1 && indexToReplace < regularIndex) {
                    segMemory[regularIndex] = MutableList(memory.size) { null }
                    val sizeDiff = segMemory[indexToReplace].size - memory.size
                    if (sizeDiff == 0) {
                        segMemory[indexToReplace] = MutableList(memory.size) { memory.first() }
                    } else {
                        segMemory[indexToReplace] = MutableList(sizeDiff) { null }
                        segMemory.add(indexToReplace, memory)
                    }
                }
            }

            return segMemory.flatten().checksum()
        }
    }
}

fun main() {

    val day = "Day09"

    val testInput = readInput("${day}_test").first()
    check(Day09.part1(testInput).also(::println) == Day09.TEST1)
    check(Day09.part2(testInput).also(::println) == Day09.TEST2)

    val input = readInput(day).first()

    measureTime {
        Day09.part1(input).println() // 6288707484810
    }.also { println("Part1 took $it") }

    measureTime {
        Day09.part2(input).println()
    }.also { println("Part2 took $it") }
}