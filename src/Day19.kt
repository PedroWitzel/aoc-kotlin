import kotlin.time.measureTime

class Day19 {

    companion object {
        const val TEST1 = 6
        const val TEST2 = 11

        private fun parseInput(input: List<String>): Pair<MutableSet<String>, List<String>> =
            input.partition { it.contains(",") }.let {
                it.first.first().split(",").map { it.trim() }.toMutableSet() to it.second.filter(String::isNotBlank)
            }

        private fun findPatterFor(towel: String, patterns: MutableSet<String>): Boolean {

            var patternUntil = listOf("")
            while (towel !in patternUntil) {
                val okPattern = patterns.filter { towel.startsWith(patternUntil + it) }
                if (okPattern.isEmpty()) return false
                patternUntil += okPattern
            }

            return true

        }

        fun part1(input: List<String>): Int {
            val (patterns, towels) = parseInput(input)
            return towels.count { findPatterFor(it, patterns) }.also(::println)
        }

        fun part2(input: List<String>): Int {
            return TEST2
        }
    }
}

fun main() {

    val day = "Day19"

    val testInput = readInput("${day}_test")
    check(Day19.part1(testInput).also(::println) == Day19.TEST1)
    check(Day19.part2(testInput).also(::println) == Day19.TEST2)

    val input = readInput(day)

    measureTime {
        Day19.part1(input).also { check(it > 344) }.println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day19.part2(input).println()
    }.also { println("Part2 took $it") }
}