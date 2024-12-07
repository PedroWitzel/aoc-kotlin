import kotlin.math.pow
import kotlin.time.measureTime

fun main() {

    val day = "Day07"
    val test1 = 3749L
    val test2 = 11387L
    val permutationsCache = emptyMap<Int, List<String>>().toMutableMap()

    fun parseInput(input: List<String>) = input.map { line: String ->
        line.split(':').let { groups ->
            Pair(
                groups.first().toLong(),
                groups.last().split(' ').mapNotNull { it.toIntOrNull() }
            )
        }
    }

    fun Int.pow(n: Int): Int = this.toDouble().pow(n.toDouble()).toInt()

    fun canResolve(values: List<Int>, result: Long, operations: Int): Boolean {
        val permutationsCount = operations.pow(values.size - 1)
        return permutationsCache
            .getOrPut(permutationsCount) {
                (0..<permutationsCount).map { i ->
                    i.toString(operations).padStart(values.size - 1, '0')
                }
            }
            .any { ops ->
                var acc = values.first().toLong()
                ops.forEachIndexed { i, c ->
                    when (c) {
                        '0' -> acc += values[i + 1]
                        '1' -> acc *= values[i + 1]
                        '2' -> acc = "$acc${values[i + 1]}".toLong()
                        else -> throw Exception("Unreachable code")
                    }
                }
                result == acc
            }
    }

    fun part1(input: List<String>): Long {
        return parseInput(input)
            .filter { (result, values) ->
                canResolve(values, result, 2)
            }
            .sumOf { (result, _) -> result }
    }

    fun part2(input: List<String>): Long {
        return parseInput(input)
            .filter { (result, values) ->
                canResolve(values, result, 3)
            }
            .sumOf { (result, _) -> result }
    }

    val testInput = readInput("${day}_test")
    check(part1(testInput) == test1)
    check(part2(testInput) == test2)

    val input = readInput(day)
    measureTime {
        part1(input).also { check(it == 4555081946288L) }.println()
    }.also { println("Part1 took $it") }
    measureTime {
        part2(input).also { check(it == 227921760109726L) }.println()
    }.also { println("Part2 took $it") }
}
