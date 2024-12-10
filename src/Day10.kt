import kotlin.time.measureTime

class Day10 {

    class Map(val input: List<String>) {

        var starts = mutableSetOf<Position>()
        val map = input.mapIndexed { i, line ->
            line.mapIndexed { j, it ->
                val c = it.digitToInt()
                if (c == 0) starts.add(Position(i, j))
                c
            }
        }

        val border = Position(map.size - 1, map.first().size - 1)

        fun heightOf(pos: Position) = map[pos.x][pos.y]

        fun marchToEnd(pos: Position): Sequence<Position> {

            if (heightOf(pos) == 9) return sequenceOf(pos)

            return pos.neighbors(border).asSequence().filter { heightOf(it) - heightOf(pos) == 1 }.flatMap(::marchToEnd)
        }
    }

    companion object {

        const val TEST1 = 36
        const val TEST2 = 81

        fun part1(input: List<String>): Int {
            val map = Map(input)
            return map.starts.sumOf { map.marchToEnd(it).toSet().size }
        }

        fun part2(input: List<String>): Int {
            val map = Map(input)
            return map.starts.sumOf { map.marchToEnd(it).toList().size }
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
        Day10.part1(input).also { check(it == 820) }.println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day10.part2(input).also { check(it == 1786) }.println()
    }.also { println("Part2 took $it") }
}