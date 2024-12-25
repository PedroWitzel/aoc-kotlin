import kotlin.time.measureTime

class Day25 {

    companion object {
        const val TEST1 = 3
        const val TEST2 = 11

        private fun parseInput(input: String): Pair<List<Array<Int>>, List<Array<Int>>> {

            val locks = emptyList<Array<Int>>().toMutableList()
            val keys = emptyList<Array<Int>>().toMutableList()
            input.split("\r\n\r\n")
                    .forEach { pattern ->
                        val isLock = pattern.first() == '#'

                        val entry = arrayOf(0, 0, 0, 0, 0)
                        pattern.lines()
                                .forEachIndexed { row, line ->
                                    line.forEachIndexed { col, ch ->
                                        if (isLock && ch == '#') entry[col] = row
                                        else if (!isLock && ch == '.') entry[col] = 5 - row
                                    }
                                }
                        if (isLock) locks.add(entry)
                        else keys.add(entry)
                    }

            return Pair(locks, keys)

        }

        fun part1(input: String): Int {

            val (locks, keys) = parseInput(input)

            return locks.sumOf { lock ->
                val indexedLock = lock.withIndex()
                keys.count { key ->
                    indexedLock.all { (index, value) ->
                        key[index] + value <= 5
                    }
                }
            }
        }

        fun part2(input: String): Int {
            return TEST2
        }
    }
}

fun main() {

    val day = "Day25"

    val testInput = readInputAsLine("${day}_test")
    check(
        Day25.part1(testInput)
                .also(::println) == Day25.TEST1
    )
    check(
        Day25.part2(testInput)
                .also(::println) == Day25.TEST2
    )

    val input = readInputAsLine(day)

    measureTime {
        Day25.part1(input)
                .println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day25.part2(input)
                .println()
    }.also { println("Part2 took $it") }
}