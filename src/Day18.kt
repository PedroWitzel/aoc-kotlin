import Direction.EAST
import kotlin.time.measureTime

class Day18 {

    companion object {
        const val TEST1 = 22
        const val TEST2 = "6,1"

        fun parseInput(input: List<String>, seconds: Int): List<Position> = buildList {
            input.forEachIndexed { i, pos ->
                if (i == seconds) return this
                val (x, y) = pos.split(",").map { it.toInt() }
                add(Position(x, y))
            }
        }

        data class Historian(val position: Position, var direction: Direction, var value: Int = 0) {

            fun step(
                corruptedMemory: Set<Position>, visitedPositions: Map<Position, Historian>, border: Position
            ): List<Historian> {

                fun nextPosition(direction: Direction): Historian? {
                    val nextPosition = position.walkTo(direction)
                    if (nextPosition.isInbound(border) && !corruptedMemory.contains(nextPosition)) {
                        val nextValue = this.value + 1

                        val alreadySeen = visitedPositions[nextPosition]
                        if (alreadySeen != null) {
                            if (alreadySeen.value > nextValue) {
                                alreadySeen.value = nextValue
                                alreadySeen.direction = direction
                                return alreadySeen
                            }
                        } else {
                            return Historian(nextPosition, direction, nextValue)
                        }
                    }

                    return null
                }

                return buildList {
                    add(nextPosition(direction))
                    add(nextPosition(direction.rotateRight()))
                    add(nextPosition(direction.rotateLeft()))
                }.filterNotNull()
            }
        }

        fun draw(border: Position, blocks: Set<Position>, path: MutableMap<Position, Historian>) {
            (0..<border.x).forEach { i ->
                (0..<border.y).forEach { j ->
                    val here = Position(i, j)
                    if (path.contains(here)) print("[${path[here]?.value}]".padEnd(8, ' '))
                    else if (blocks.contains(here)) print("#".padEnd(8, '#'))
                    else print(".".padEnd(8, '.'))
                }
                println("")
            }
            println("")
        }

        fun part1(input: List<String>, border: Position, seconds: Int): Int {
            val corruptedMemory = parseInput(input, seconds).toSet()
            val position = Position(0, 0)
            val endPosition = border.let { Position(it.x - 1, it.y - 1) }
            var robot = Historian(position, EAST, 0)
            var seenRobots = mapOf(robot.position to robot).toMutableMap()
            val unvisitedRobots = listOf(robot).toMutableList()
            seenRobots[robot.position] = robot

            while (unvisitedRobots.isNotEmpty()) {
                robot = unvisitedRobots.removeFirst()
                seenRobots[robot.position] = robot
                val nextSteps = robot.step(corruptedMemory, seenRobots, border)
                nextSteps.forEach { seenRobots.getOrPut(it.position) { it } }
                unvisitedRobots.addAll(nextSteps)
            }

            return seenRobots[endPosition]?.value ?: 0
        }

        fun part2(input: List<String>, border: Position, seconds: Int): String {

            val corruptedMemory = parseInput(input, -1)
            val endPosition = border.let { Position(it.x - 1, it.y - 1) }
            var hasEndPosition = false
            var currentTry = seconds

            do {
                val moreCorruption = corruptedMemory.slice(0..currentTry++).toSet()
                var robot = Historian(Position(0, 0), EAST, 0)
                val seenRobots = mapOf(robot.position to robot).toMutableMap()
                val unvisitedRobots = listOf(robot).toMutableList()
                seenRobots[robot.position] = robot
                while (unvisitedRobots.isNotEmpty()) {
                    robot = unvisitedRobots.removeFirst()
                    seenRobots[robot.position] = robot
                    val nextSteps = robot.step(moreCorruption, seenRobots, border)
                    nextSteps.forEach { seenRobots.getOrPut(it.position) { it } }
                    unvisitedRobots.addAll(nextSteps)
                }
                hasEndPosition = seenRobots.containsKey(endPosition)
            } while (hasEndPosition)


            val newBlock = corruptedMemory.elementAt(currentTry - 1)
            return "${newBlock.x},${newBlock.y}"
        }
    }
}

fun main() {

    val day = "Day18"

    val testInput = readInput("${day}_test")
    check(Day18.part1(testInput, Position(7, 7), 12).also(::println) == Day18.TEST1)
    check(Day18.part2(testInput, Position(7, 7), 12).also(::println) == Day18.TEST2)

    val input = readInput(day)

    measureTime {
        Day18.part1(input, Position(71, 71), 1024).also { check(it == 348) }.println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day18.part2(input, Position(71, 71), 1024).println()
    }.also { println("Part2 took $it") }
}
