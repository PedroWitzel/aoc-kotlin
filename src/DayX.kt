import kotlin.time.measureTime

fun main() {

    val day = "DayXX"
    val test1 = 11
    val test2 = 11

    fun part1(input: List<String>): Int {
        return test1
    }

    fun part2(input: List<String>): Int {
        return test2
    }

    val testInput = readInput("${day}_test")
    check(part1(testInput).also(::println) == test1)
    check(part2(testInput).also(::println) == test2)

    val input = readInput(day)

    measureTime {
        part1(input).println()
    }.also { println("Part1 took $it") }

    measureTime {
        part2(input).println()
    }.also { println("Part1 took $it") }
}
