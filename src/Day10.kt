import kotlin.time.measureTime

class Day10 {

    class Map(val input: List<String>) {

        // TODO - test it .asSequence() on benchy
        val map: List<List<Int>> = input.map { line ->
            line.map { it.digitToInt() }
        }

        val border = Position(map.size - 1, map.first().size - 1)

        fun starts(): Set<Position> = buildSet {
            map.forEachIndexed { i, row ->
                row.forEachIndexed { j, col ->
                    if (col == 0) add(Position(i, j))
                }
            }
        }

        fun heightOf(pos: Position) = map[pos.x][pos.y]

        fun marchToEnd(pos: Position): List<Position> {

            if (heightOf(pos) == 9) return listOf(pos)

            return pos.neighbors(border).filter { heightOf(it) - heightOf(pos) == 1 }.flatMap(::marchToEnd)
        }
    }

    companion object {

        const val TEST1 = 36
        const val TEST2 = 81

        fun part1(input: List<String>): Int {
            val map = Map(input)
            return map.starts().sumOf { map.marchToEnd(it).distinct().size }
        }

        fun part2(input: List<String>): Int {
            val map = Map(input)
            return map.starts().sumOf { map.marchToEnd(it).size }
        }
    }
}

fun main() {

    val day = "Day10"

    val testInput = readInput("${day}_test")
    check(Day10.part1(testInput).also(::println) == Day10.TEST1)
    check(Day10.part2(testInput).also(::println) == Day10.TEST2)

    val input = readInput(day)

    measureTime {
        Day10.part1(input).println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day10.part2(input).println()
    }.also { println("Part2 took $it") }
}