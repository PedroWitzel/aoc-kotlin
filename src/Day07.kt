fun main() {

    val day = "Day07"
    val test1 = 3749L
    val test2 = 11387L

    fun parseInput(input: List<String>) = input.filter(String::isNotBlank).map { line: String ->
        line.split(':').let { groups ->
            Pair(
                groups.first().toLong(),
                groups.last().let { numbers ->
                    numbers.split(' ').mapNotNull { it.toIntOrNull() }
                }
            )
        }
    }

    fun Int.exp(n: Int): Int {
        var acc = this
        repeat(n - 1) { acc *= this }
        return acc
    }

    fun canResolve(values: List<Int>, result: Long, operations: Array<Char>): Boolean {
        val permutationsCount = operations.size.exp(values.size - 1)
        val permutations = emptyList<String>().toMutableList()
        repeat(permutationsCount) { i ->
            permutations.add(
                i.toString(operations.size).padStart(values.size - 1, '0')
                    .map { c -> operations[c.digitToInt(operations.size)] }
                    .joinToString("")
            )
        }

        return permutations.any { ops ->
            var acc = values.first().toLong()
            ops.forEachIndexed { i, c ->
                when (c) {
                    '+' -> acc += values[i + 1]
                    '*' -> acc *= values[i + 1]
                    '|' -> acc = (acc.toString() + values[i + 1].toString()).toLong()
                    else -> {}
                }
            }
            result == acc
        }
    }

    fun part1(input: List<String>): Long {
        val operations = arrayOf('+', '*')
        return parseInput(input)
            .filter { (result, values) ->
                canResolve(values, result, operations)
            }
            .sumOf { (result, _) -> result }
            .also(::println)
    }

    fun part2(input: List<String>): Long {
        val operations = arrayOf('+', '*', '|')
        return parseInput(input)
            .filter { (result, values) ->
                canResolve(values, result, operations)
            }
            .sumOf { (result, _) -> result }
            .also(::println)
    }

    val testInput = readInput("${day}_test")
    check(part1(testInput) == test1)
    check(part2(testInput) == test2)

    val input = readInput(day)
    part1(input).also { check(it == 4555081946288L) }.println()
    part2(input).also { check(it == 227921760109726L) }.println()
}
