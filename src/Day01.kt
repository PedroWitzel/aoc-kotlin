import kotlin.math.abs

fun main() {
    fun readInputLists(input: List<String>): Pair<MutableList<Int>, MutableList<Int>> {
        val right = mutableListOf<Int>()
        val left = mutableListOf<Int>()
        input.forEach {
            val splitString = it.split(" ")
            right.add(splitString.first().toInt())
            left.add(splitString.last().toInt())
        }
        return Pair(right, left)
    }

    fun part1(input: List<String>): Int {

        val (right, left) = readInputLists(input)

        right.sort()
        left.sort()

        return right.zip(left).sumOf { abs(it.first - it.second) }
    }

    fun part2(input: List<String>): Int {
        val (right, left) = readInputLists(input)
        return right.sumOf { rightNum -> rightNum * left.count { leftNum -> rightNum == leftNum } }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
