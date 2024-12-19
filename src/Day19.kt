import kotlin.time.measureTime

class Day19 {

    companion object {
        const val TEST1 = 6
        const val TEST2 = 16L

        private fun parseInput(input: List<String>): Pair<Set<String>, List<String>> =
            input.partition { it.contains(",") }.let {
                it.first.first().split(",").map { it.trim() }.toMutableSet() to it.second.filter(String::isNotBlank)
            }

        private fun String.arrangeTowels(towels: Set<String>): Long {
            val cache = mutableMapOf<Int, Long>()
            fun options(pos: Int): Long = when {
                pos == length -> 1
                else -> cache.getOrPut(pos) {
                    towels.sumOf { towel ->
                        if (startsWith(towel, pos)) options(pos + towel.length) else 0
                    }
                }
            }
            return options(0)
        }

        fun solve(input: List<String>): Pair<Int, Long> {
            val (towels, patterns) = parseInput(input)
            return patterns.map { it.arrangeTowels(towels) }.let {
                it.count { it > 0 } to it.sumOf { it }
            }
        }
    }
}

fun main() {

    val day = "Day19"

    val testInput = readInput("${day}_test")
    check(Day19.solve(testInput).also(::println) == (Day19.TEST1 to Day19.TEST2))

    val input = readInput(day)

    measureTime {
        Day19.solve(input).also { check(it.first == 367) }.println()
    }.also { println("Part1 took $it") }
}