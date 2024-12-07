import kotlin.time.measureTime


fun main() {

    val day = "Day05"
    val test1 = 143
    val test2 = 123

    fun parseInput(input: List<String>) =
        input.partition { it.contains('|') }.let { (pairs, sequence) ->
            Pair(
                pairs
                    .map { orderDep ->
                        orderDep.split('|')
                            .let { Pair(it.first().toInt(), it.last().toInt()) }
                    }
                    .groupBy { it.first }
                    .map { (k, v) -> k to v.map { it.second } }
                    .associate { it.first to it.second },
                sequence
                    .filter(String::isNotBlank)
                    .map { pages -> pages.split(',').map { it.toInt() } }
            )
        }

    fun List<Int>.middleIndex() = (this.size / 2 + if (this.size % 2 != 0) 1 else 0) - 1

    fun isOrdered(
        sequence: List<Int>,
        dependencies: Map<Int, List<Int>>,
    ): Boolean {
        var valid = true
        sequence.forEachIndexed { index, i ->
            if (index != 0) {
                val previousSlice = sequence.subList(0, index)
                valid = valid && dependencies[i]?.all { dep: Int ->
                    !previousSlice.contains(dep)
                } ?: true
            }
        }
        return valid
    }

    fun part1(input: List<List<Int>>): Int {
        return input.sumOf { it[it.middleIndex()] }
    }

    fun part2(input: List<List<Int>>, dependencies: Map<Int, List<Int>>): Int {
        return input
            .map {
                it.sortedWith { a, b ->
                    if (dependencies[a]?.contains(b) == true) -1
                    else 1
                }
            }
            .sumOf { it[it.middleIndex()] }
    }

//    val testInput = readInput("${day}_test")
//    check(part1(testInput) == test1)
//    check(part2(testInput) == test2)

    val input = readInput(day)
    measureTime {
        val (dependencies, sequences) = parseInput(input)
        val (goods, bads) = sequences.partition { isOrdered(it, dependencies) }
        part1(goods).also { check(it == 6505) }.println()
        part2(bads, dependencies).also { check(it == 6897) }.println()
    }.also(::println)
}
