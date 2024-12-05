fun main() {

    val day = "Day05"
    val test1 = 143
    val test2 = 123

    fun parseInput(input: List<String>) =
        input.partition { it.contains('|') }.let { (pairs, sequence) ->
            Pair(
                pairs
                    .map { orderDep ->
                        orderDep.split('|')
                            .let { Pair(it.first().toInt(), it.last().toInt()) }
                    }
                    .groupBy { it.first }
                    .map { (k, v) -> k to v.map { it.second } }
                    .associate { it.first to it.second },
                sequence
                    .filter { it.isNotBlank() }
                    .map { pages -> pages.split(',').map { it.toInt() } }
            )
        }

    fun List<Int>.middleIndex() = (this.size / 2 + if (this.size % 2 != 0) 1 else 0) - 1

    fun isOrdered(
        sequence: List<Int>,
        dependencies: Map<Int, List<Int>>,
    ): Boolean {
        var valid = true
        sequence.forEachIndexed { index, i ->
            if (index != 0) {
                val previousSlice = sequence.subList(0, index)
                valid = valid && dependencies[i]?.all { dep: Int ->
                    !previousSlice.contains(dep)
                } ?: true
            }
        }
        return valid
    }

    fun part1(input: List<String>): Int {

        val (dependencies, sequences) = parseInput(input)
        return sequences
            .filter { isOrdered(it, dependencies) }
            .sumOf { it[it.middleIndex()] }
    }

    fun part2(input: List<String>): Int {
        val (dependencies, sequences) = parseInput(input)
        return sequences
            .filter { !isOrdered(it, dependencies) }
            .map {
                it.sortedWith { a, b ->
                    if (dependencies[a]?.contains(b) == true) -1
                    else 1
                }
            }
            .sumOf { it[it.middleIndex()] }
    }

    val testInput = readInput("${day}_test")
    check(part1(testInput) == test1)
    check(part2(testInput) == test2)

    val input = readInput(day)
    part1(input).println() // 6505
    part2(input).println() // 6897
}
