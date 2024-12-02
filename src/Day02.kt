import kotlin.math.sign

fun main() {

    val day = "Day02"
    val test1 = 2
    val test2 = 6

    fun isReportSafe(levels: List<Int>) = levels.all { levels.first().sign * it in 1..3 }

    fun neighborDistance(it: List<Int>) = it.zipWithNext { a, b -> a - b }

    fun parseReports(line: String) = line.split(' ').map { it.toInt() }

    fun part1(input: List<String>): Int {
        return input
            .map(::parseReports)
            .map(::neighborDistance)
            .count(::isReportSafe)
    }

    fun part2(input: List<String>): Int {
        val reports = input.map(::parseReports)

        val (goods, maybeBad) = reports.partition {
            val diffLevels = neighborDistance(it)
            isReportSafe(diffLevels) || isReportSafe(diffLevels.subList(1, diffLevels.size))
        }

        val almostGood = maybeBad.count { levels ->
            val diffLevels = neighborDistance(levels)
            val badReportIndex = diffLevels.indexOfFirst { diffLevels.first().sign * it !in 1..3 }
            isReportSafe(neighborDistance(levels.filterIndexed { i, _ -> i != badReportIndex })) ||
                    isReportSafe(neighborDistance(levels.filterIndexed { i, _ -> i != badReportIndex + 1 }))
        }

        return goods.size + almostGood
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("${day}_test")
    check(part1(testInput) == test1)
    check(part2(testInput) == test2)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput(day)
    part1(input).println() // 624
    part2(input).println() // 658
}
