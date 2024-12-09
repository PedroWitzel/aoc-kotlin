import kotlin.time.measureTime

class DayX {

    companion object {
        const val TEST1 = 11
        const val TEST2 = 11

        fun part1(input: List<String>): Int {
            return TEST1
        }

        fun part2(input: List<String>): Int {
            return TEST2
        }
    }
}

fun main() {

    val day = "DayX"

    val testInput = readInput("${day}_test")
    check(DayX.part1(testInput).also(::println) == DayX.TEST1)
    check(DayX.part2(testInput).also(::println) == DayX.TEST2)

    val input = readInput(day)

    measureTime {
        DayX.part1(input).println()
    }.also { println("Part1 took $it") }

    measureTime {
        DayX.part2(input).println()
    }.also { println("Part2 took $it") }
}
