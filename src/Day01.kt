import kotlin.math.abs

fun main() {
    fun readInputLists(input: List<String>): Pair<List<Int>, List<Int>> {
        return input.map { line ->
            line.split(" ")
                .filter(String::isNotBlank)
                .map(String::toInt)
        }
            .map { it.first() to it.last() }
            .unzip()
    }

    fun part1(input: List<String>): Int {
        val (right, left) = readInputLists(input)
        return right.sorted()
            .zip(left.sorted())
            .sumOf { abs(it.first - it.second) }
    }

    fun part2(input: List<String>): Int {
        val (right, left) = readInputLists(input)
        val leftCounts = left.groupingBy { it }.eachCount()
        return right.sumOf { it * (leftCounts[it] ?: 0) }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println() // 1223326
    part2(input).println() // 21070419
}
