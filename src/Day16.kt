import Direction.EAST
import kotlin.time.measureTime

class Day16 {

    data class Reindeer(
        val position: Position,
        val direction: Direction,
        val value: Int,
        val pathsToHere: MutableSet<Position> = mutableSetOf(position)
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Reindeer) return false
            return position == other.position && direction == other.direction
        }

        override fun hashCode(): Int = 31 * position.hashCode() + direction.hashCode()

        fun List<String>.valueOf(pos: Position) = this[pos.x][pos.y]

        fun step(map: List<String>): List<Reindeer> {

            fun nextPosition(direction: Direction): Reindeer? {
                val nextPosition = position.walkTo(direction)
                return if (map.valueOf(nextPosition) != '#') {
                    val nextValue = this.value + if (this.direction == direction) 1 else 1001
                    val pathsToThere = this.pathsToHere.toMutableSet().apply { add(nextPosition) }
                    Reindeer(nextPosition, direction, nextValue, pathsToThere)
                } else null
            }

            return buildList {
                add(nextPosition(direction))
                add(nextPosition(direction.rotateRight()))
                add(nextPosition(direction.rotateLeft()))
            }.filterNotNull()
        }
    }

    companion object {
        const val TEST1 = 11048
        const val TEST2 = 64

        fun findStartAndFinish(map: List<String>): Pair<Position, Position> {
            var start = Position(0, 0)
            var finish = Position(0, 0)
            map.forEachIndexed { i, line ->
                val startIndex = line.indexOfFirst { it == 'S' }
                if (startIndex != -1) start = Position(i, startIndex)

                val endIndex = line.indexOfFirst { it == 'E' }
                if (endIndex != -1) finish = Position(i, endIndex)
            }

            return start to finish
        }

        fun draw(map: List<String>, path: Set<Reindeer>) {
            map.forEachIndexed { i, line ->
                line.forEachIndexed { j, ch ->
                    val deer = path.find { it.position == Position(i, j) }
                    if (deer != null) print("[${deer.value};${deer.pathsToHere.size}]".padEnd(12, ' '))
                    else print("$ch".padEnd(12, ch))
                }
                println("")
            }
            println("")
        }

        fun solve(map: List<String>): Pair<Int, Int> {

            val (start, finish) = findStartAndFinish(map)

            var reindeer = Reindeer(start, EAST, 0)
            val seenDeer = emptySet<Reindeer>().toMutableSet()
            val unvisited = listOf(reindeer).toMutableList()
            var lowestValue = Int.MAX_VALUE
            val nicePositions = emptySet<Position>().toMutableSet()

            while (unvisited.isNotEmpty()) {

                reindeer = unvisited.removeFirst()

                if (reindeer.position == finish) {
                    if (reindeer.value > lowestValue) continue
                    lowestValue = reindeer.value
                    nicePositions += reindeer.pathsToHere
                }

                seenDeer.add(reindeer)

                val nextSteps = reindeer.step(map)
                unvisited.addAll(nextSteps.filter { it !in seenDeer })
                unvisited.sortBy { it.value }
            }

            val finalRobot = seenDeer.find { it.position == finish } ?: return 0 to 0
            return finalRobot.value to nicePositions.size
        }
    }
}

fun main() {

    val day = "Day16"

    val testInput = readInput("${day}_test")
    check(Day16.solve(testInput).also(::println) == Day16.TEST1 to Day16.TEST2)

    val input = readInput(day)

    measureTime {
        Day16.solve(input).also {
            check(it.first == 114476 && it.second == 508)
        }.println()
    }.also { println("Part1 took $it") }

}