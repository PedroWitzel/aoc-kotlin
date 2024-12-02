import kotlin.math.sign

fun main() {

    val day = "Day02"
    val test1 = 2
    val test2 = 6

    fun isReportSafe(levels: List<Int>) = levels.all { levels.first().sign * it in 1..3 }

    fun part1(input: List<String>): Int {
        return input.map { line ->
            line
                .split(' ').map { it.toInt() }
                .zipWithNext { a, b -> a - b }
        }.count { levels ->
            isReportSafe(levels)
        }
    }

    fun part2(input: List<String>): Int {
        val reports = input.map { line -> line.split(' ').map { it.toInt() } }

        var safeReports = 0
        reports.map { it.zipWithNext { a, b -> a - b } }.forEachIndexed { index, levels ->
            if (isReportSafe(levels) || isReportSafe(levels.subList(1, levels.size))) {
                ++safeReports
            } else {
                val badReportIndex = levels.indexOfFirst { levels.first().sign * it !in 1..3 }
                val mutList = reports[index].toMutableList()
                mutList.removeAt(badReportIndex)
                if (isReportSafe(mutList.zipWithNext { a, b -> a - b })) {
                    ++safeReports
                } else {
                    val otherSide = reports[index].toMutableList()
                    otherSide.removeAt(badReportIndex + 1)
                    if (isReportSafe(otherSide.zipWithNext { a, b -> a - b })) {
                        ++safeReports
                    }
                }
            }
        }

        return safeReports
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("${day}_test")
    check(part1(testInput) == test1)
    check(part2(testInput) == test2)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput(day)
    part1(input).println()
    part2(input).println()
}
