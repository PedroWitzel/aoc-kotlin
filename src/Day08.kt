import kotlin.time.measureTime

data class Position(val x: Int, val y: Int) {

    operator fun minus(other: Position) = Position(
        this.x - other.x,
        this.y - other.y
    )

    operator fun plus(other: Position) = Position(
        this.x + other.x,
        this.y + other.y
    )

    fun mirrorPositionFrom(other: Position) = (other - this).let { other + it }
}

data class Antenna(val pos: Position, val c: Char)

data class Antennas(val input: List<String>) {
    val antennas by lazy {
        input.flatMapIndexed { i, line ->
            line.mapIndexedNotNull { j, c ->
                if (c != '.') Antenna(Position(i, j), c) else null
            }
        }.groupBy { it.c }
    }

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

fun main() {

    val day = "Day08"
    val test1 = 14
    val test2 = 34

    fun part1(input: List<String>): Int {
        val map = Antennas(input)
        return map.antennas.values.flatMap { antennas ->
            antennas.flatMap { antenna ->
                antennas
                    .filterNot { it == antenna }
                    .map { antenna.pos.mirrorPositionFrom(it.pos) }
                    .filter { map.isInbound(it) }
            }
        }.toSet().size
    }

    fun part2(input: List<String>): Int {
        val map = Antennas(input)
        val allAntennas = map.antennas.values.flatMap { a -> a.map { it.pos } }
        return map.antennas.values.flatMap { antennas ->
            antennas.flatMap { antenna ->
                antennas
                    .filterNot { it == antenna }
                    .flatMap {
                        generateSequence(antenna.pos to it.pos) { (origin, dest) ->
                            val node = origin.mirrorPositionFrom(dest)
                            if (map.isInbound(node)) dest to node else null
                        }.map { pos -> pos.second }
                    }
            }
        }.toSet().filterNot { it in allAntennas }.size + allAntennas.size
    }

    val testInput = readInput("${day}_test")
    check(part1(testInput).also(::println) == test1)
    check(part2(testInput).also(::println) == test2)

    val input = readInput(day)

    measureTime {
        part1(input).also { check(it == 320) }.println()
    }.also { println("Part1 took $it") }

    measureTime {
        part2(input).also { check(it == 1157) }.println()
    }.also { println("Part1 took $it") }
}
