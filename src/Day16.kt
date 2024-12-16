import Direction.EAST
import kotlin.time.measureTime

class Day16 {

    data class Robot(val position: Position, var direction: Direction, var value: Int) {

        fun List<String>.valueOf(pos: Position) = this[pos.x][pos.y]

        fun step(map: List<String>, seenRobots: Map<Position, Robot>): List<Robot> {

            fun nextPosition(direction: Direction): Robot? {
                val nextPosition = position.walkTo(direction)
                var nextRobot: Robot? = null
                if (map.valueOf(nextPosition) != '#') {
                    val nextValue = this.value + if (this.direction == direction) 1 else 1001

                    val alreadySeen = seenRobots[nextPosition]
                    if (alreadySeen != null) {
                        if (alreadySeen.value > nextValue) {
                            alreadySeen.value = nextValue
                            alreadySeen.direction = direction
                            nextRobot = alreadySeen
                        }
                    } else {
                        nextRobot = Robot(nextPosition, direction, nextValue)
                    }
                }

                return nextRobot
            }

            return buildList {
                add(nextPosition(direction))
                add(nextPosition(direction.rotateRight()))
                add(nextPosition(direction.rotateLeft()))
            }.filterNotNull()
        }

        fun stepBack(
            seenRobots: Map<Position, Robot>,
            seenPositions: Set<Position>,
            previousRobot: Robot
        ): List<Robot> {

            fun previousPosition(direction: Direction): Robot? {
                val nextPosition = position.walkTo(direction)
                if (nextPosition in seenPositions) return null

                val nextRobot = seenRobots[nextPosition]
                if (nextRobot == null) return null

                if (value > nextRobot.value || previousRobot.value - nextRobot.value == 2) return nextRobot

                return null
            }

            return buildList {
                add(previousPosition(direction))
                add(previousPosition(direction.rotateRight()))
                add(previousPosition(direction.rotateLeft()))
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

        fun draw(map: List<String>, path: MutableMap<Position, Robot>, nicePosition: Set<Position>) {
            map.forEachIndexed { i, line ->
                line.forEachIndexed { j, ch ->
                    val pos = Position(i, j)
                    if (pos in nicePosition) print("[${path[pos]?.value}]".padEnd(8, ' '))
                    else print("$ch".padEnd(8, ch))
                }
                println("")
            }
            println("")

        }

        fun solve(map: List<String>): Pair<Int, Int> {

            val (start, finish) = findStartAndFinish(map)

            var robot = Robot(start, EAST, 0)
            val seenRobots = mapOf(robot.position to robot).toMutableMap()
            val unvisitedRobots = listOf(robot).toMutableList()
            seenRobots[robot.position] = robot

            while (unvisitedRobots.isNotEmpty()) {
                robot = unvisitedRobots.removeFirst()
                seenRobots[robot.position] = robot

                if (robot.position == finish) continue

                val nextSteps = robot.step(map, seenRobots)

                nextSteps.forEach { seenRobots.getOrPut(it.position) { it } }

                unvisitedRobots.addAll(nextSteps)
            }

            val finalRobot = seenRobots[finish] ?: return 0 to 0
            val finalValue = finalRobot.value

            // Part2
            var previousRobot: Robot = finalRobot
            var backRobot = finalRobot
            val unvisitedTraceBack = listOf(backRobot).toMutableList()
            val nicePosition = emptySet<Position>().toMutableSet()

            while (unvisitedTraceBack.isNotEmpty()) {

                previousRobot = backRobot
                backRobot = unvisitedTraceBack.removeFirst()
                backRobot.direction = backRobot.direction.opposite()

                nicePosition.add(backRobot.position)
                if (backRobot.position == start) continue

                val nextSteps = backRobot.stepBack(seenRobots, nicePosition, previousRobot)
                unvisitedTraceBack.addAll(nextSteps)
            }
//            draw(map, seenRobots, nicePosition)


            return finalValue to nicePosition.size
        }
    }
}

fun main() {

    val day = "Day16"

    val testInput = readInput("${day}_test")
    check(Day16.solve(testInput).also(::println) == Day16.TEST1 to Day16.TEST2)

    val input = readInput(day)

    measureTime {
        Day16.solve(input).also { check(it.first == 114476 && it.second == 503) }.println()
    }.also { println("Part1 took $it") }

}