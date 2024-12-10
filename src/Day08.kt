import kotlin.time.measureTime


data class Antennas(val input: List<String>) {
    val antennas = input.flatMapIndexed { i, line ->
        line.mapIndexedNotNull { j, c ->
            if (c != '.') c to Position(i, j) else null
        }
    }.groupBy({ it.first }, { it.second })

    private val bounds = Position(input.size, input.first().length)

    fun isInbound(pos: Position) = pos.x in 0..<bounds.x && pos.y in 0..<bounds.y

    fun printWithNodes(nodes: List<Position>) {
        input.forEachIndexed { i, s ->
            s.forEachIndexed { j, c ->
                if (c == '.' && Position(i, j) in nodes) print('#')
                else print(c)
            }
            print('\n')
        }
    }
}

class Day08 {

    companion object {

        fun oldPart1(input: List<String>): Int {
            val map = Antennas(input)
            return map.antennas.values.flatMap { antennas ->
                antennas.flatMap { antenna ->
                    antennas
                        .filterNot { it == antenna }
                        .map { antenna.mirrorPositionFrom(it) }
                        .filter { map.isInbound(it) }
                }
            }.toSet().size
        }

        fun part1(input: List<String>): Int {
            val map = Antennas(input)
            return buildSet {
                map.antennas.values.forEach { antennas ->
                    antennas.forEachIndexed { idx, antenna ->
                        antennas.drop(idx + 1).forEach {
                            add(antenna.mirrorPositionFrom(it))
                            add(it.mirrorPositionFrom(antenna))
                        }
                    }
                }
            }.count { map.isInbound(it) }
        }

        fun part2(input: List<String>): Int {
            val map = Antennas(input)
            val allAntennas = map.antennas.values.flatten()
            return map.antennas.values.flatMap { antennas ->
                antennas.flatMap { antenna ->
                    antennas.filterNot { it == antenna }.flatMap {
                        generateSequence(antenna to it) { (origin, dest) ->
                            val node = origin.mirrorPositionFrom(dest)
                            if (map.isInbound(node)) dest to node else null
                        }.map { pos -> pos.second }
                    }
                }
            }.toSet().filterNot { it in allAntennas }.size + allAntennas.size
        }
    }

}

fun main() {

    val day = "Day08"
    val test1 = 14
    val test2 = 34

    val testInput = readInput("${day}_test")
    check(Day08.part1(testInput).also(::println) == test1)
    check(Day08.part2(testInput).also(::println) == test2)

    val input = readInput(day)
    measureTime {
        Day08.oldPart1(input).also { check(it == 320) }.println()
    }.also { println("old Part1 took $it") }
    measureTime {
        Day08.part1(input).also { check(it == 320) }.println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day08.part2(input).also { check(it == 1157) }.println()
    }.also { println("Part1 took $it") }
}