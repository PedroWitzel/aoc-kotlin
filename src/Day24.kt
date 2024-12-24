import kotlin.time.measureTime

class Day24 {

    companion object {
        const val TEST1 = 2024L
        const val TEST2 = 11

        private fun parseInput(input: String) = input.split("\r\n\r\n")
            .let { (rules, operations) ->
                rules.lines()
                    .associate { rule ->
                        rule.split(": ")
                            .let { (id, rule) -> id to rule.toInt() }
                    }
                    .toMutableMap() to operations.lines()
                    .map {
                        val op = it.split(' ')
                            .toMutableList()
                        op.removeAt(3)
                        op.toList()
                    }
            }

        private fun calculate(a: Int, b: Int, op: String) = when (op) {
            "XOR" -> a.xor(b)
            "AND" -> a.and(b)
            "OR" -> a.or(b)
            else -> 0
        }

        private fun List<String>.doOperation(values: MutableMap<String, Int>, operations: List<List<String>>): Int {

            val leftOp = values.getOrPut(get(0)) {
                operations.getEquationFor(
                    get(0)
                )
                    ?.doOperation(values, operations) ?: 0
            }

            val rightOp = values.getOrPut(get(2)) {
                operations.getEquationFor(
                    get(2)
                )
                    ?.doOperation(values, operations) ?: 0
            }
            return calculate(leftOp, rightOp, get(1))
        }

        private fun List<List<String>>.getEquationFor(a: String) =
            find { it.last() == a }

        fun part1(input: String): Long {

            val (v, operations) = parseInput(input)

            operations
                .filter {
                    it.last()
                        .startsWith('z')
                }
                .map {
                    v[it.last()] = it.doOperation(v, operations)
                }

            return fromBinaryToInt(v, 'z')
        }

        private fun fromBinaryToInt(v: MutableMap<String, Int>, register: Char): Long =
            v.filter { it.key.startsWith(register) }
                .toSortedMap().values.toList()
                .foldRight(0L) { it, acc ->
                    acc.shl(1) + it
                }


        private fun outputFrom(gate: String, operations: List<List<String>>): String? {

            val op = operations.filter { it[0] == gate || it[2] == gate }

            op.find { it[3].startsWith('z') }
                ?.let {
                    val previousZ = it[3].drop(1)
                        .toInt()
                    return "z${previousZ - 1}"
                }

            return op.firstNotNullOfOrNull { outputFrom(it[3], operations) }
        }

        fun part2(input: String): String {
            var (v, operations) = parseInput(input)

            val badZ = operations.filter {
                it[3].startsWith('z') && it[3] != "z45" && it[1] != "XOR"
            }
                .map { it.toMutableList() }

            val badXY = operations.filter {
                it[0].first() !in "xy" && it[2].first() !in "xy" && !it[3].startsWith('z') && it[1] == "XOR"
            }
                .map { it.toMutableList() }

            operations = operations.filterNot { it in badZ + badXY }

            badXY.forEach { badStart ->
                val oldOutput = badStart[3]
                val b = badZ.find { it[3] == outputFrom(oldOutput, operations) }!!
                badStart[3] = b[3]
                b[3] = oldOutput
            }

            val x = fromBinaryToInt(v, 'x')
            val y = fromBinaryToInt(v, 'y')

            operations = operations + badZ + badXY
            operations
                .filter {
                    it.last()
                        .startsWith('z')
                }
                .map {
                    v[it.last()] = it.doOperation(v, operations)
                }

            val z = fromBinaryToInt(v, 'z')
            val missingBy = ((x + y) xor z).toString(2)
                .count { it == '0' }

            val missingSwap = operations.filter {
                it[0] == "x$missingBy" || it[2] == "x$missingBy" ||
                        it[0] == "y$missingBy" || it[2] == "y$missingBy"
            }

            return (badZ + badXY + missingSwap).map { it[3]}.sorted().joinToString(",")
        }
    }
}

fun main() {

    val day = "Day24"

    val testInput = readInputAsLine("${day}_test")
    check(
        Day24.part1(testInput)
            .also(::println) == Day24.TEST1
    )

    val input = readInputAsLine(day)

    measureTime {
        Day24.part1(input)
            .also { check(it == 49430469426918L) }
            .println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day24.part2(input)
            .println()
    }.also { println("Part2 took $it") }
}
