import kotlin.time.measureTime

class Day19 {

    companion object {
        const val TEST1 = 6
        const val TEST2 = 11

        private fun parseInput(input: List<String>): Pair<List<String>, List<String>> =
            input.partition { it.contains(",") }.let {
                it.first.first().split(",").map { it.trim() } to it.second.filter(String::isNotBlank)
            }

        private fun findPatterFor(towels: List<String>, patterns: List<String>): Boolean {
            var nextTowels = towels.toMutableList()
            while (nextTowels.isNotEmpty()) {
                val towel = nextTowels.removeFirst()

                if (towel in patterns) return true

                val towelWithPattern = patterns.mapNotNull { pattern ->
                    if (towel.startsWith(pattern)) towel.substring(pattern.length)
                    else null
                }
                nextTowels.addAll(towelWithPattern)

            }
            return false
        }

        fun part1(input: List<String>): Int {
            val (patterns, towels) = parseInput(input)
            return towels.filter { findPatterFor(listOf(it), patterns) }.also { it.println() }.size
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
        Day19.part1(input).println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day19.part2(input).println()
    }.also { println("Part2 took $it") }
}