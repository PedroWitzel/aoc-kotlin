import Direction.NORTH
import kotlin.time.measureTime

class Day20 {

    companion object {
        const val TEST1 = 11
        const val TEST2 = 285

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

        fun List<String>.valueOf(pos: Position) = this[pos.x][pos.y]

        data class Robot(val position: Position, val direction: Direction, val border: Position)

        private fun Robot.next(direction: Direction): Robot? {
            val nextPosition = position.walkTo(direction)
            return if (nextPosition.isInbound(border)) Robot(nextPosition, direction, border) else null
        }

        private fun Robot.valueOf(map: List<String>) = map.valueOf(this.position)
        fun Robot.nextPositionTo(direction: Direction, map: List<String>): Robot? {
            val next = next(direction)
            return if (next?.valueOf(map) != '#') next else null
        }

        fun Robot.nextWall(direction: Direction, map: List<String>): Robot? {
            val next = next(direction)
            return if (next?.valueOf(map) == '#') next else null
        }

        private fun Robot.step(map: List<String>): List<Robot> {
            return buildList {
                add(nextPositionTo(direction, map))
                add(nextPositionTo(direction.rotateRight(), map))
                add(nextPositionTo(direction.rotateLeft(), map))
            }.filterNotNull()
        }

        private fun Robot.tunneling(map: List<String>): List<Robot> {
            return buildList {
                listOfNotNull(
                    nextWall(direction, map),
                    nextWall(direction.rotateRight(), map),
                    nextWall(direction.rotateLeft(), map),
                ).forEach { addAll(it.step(map)) }
            }
        }

        fun getWay(map: List<String>): List<Robot> {
            val (start, finish) = findStartAndFinish(map)
            var robot = Robot(start, NORTH, Position(map.size, map.first().length))
            val path = listOf(robot).toMutableList()

            while (robot.position != finish) {
                robot = robot.step(map)
                        .first()
                path.add(robot)
            }

            return path
        }

        fun part1(map: List<String>, threshold: Int): Map<Int, Int> {

            val path = getWay(map)

            val allCuts = emptyList<Int>().toMutableList()
            path.forEachIndexed { pos, from ->
                from.tunneling(map)
                        .asSequence()
                        .map(Robot::position)
                        .map { path.indexOfFirst { r -> r.position == it } - pos - 2 }
                        .filter { it >= threshold }
                        .also { allCuts.addAll(it) }
            }

            return allCuts.groupingBy { it }
                    .eachCount()
        }

        fun part2(map: List<String>, threshold: Int): Int {

            val path = getWay(map).map { it.position }

            return path.flatMapIndexed { index, pos ->
                path.subList(index + 1, path.size)
                        .asSequence()
                        .map { it to pos.straightDistanceTo(it) }
                        .filter { it.second <= 20 }
                        .map { path.indexOf(it.first) - index - it.second }
                        .filter { it >= threshold }
            }
                    .groupingBy { it }
                    .eachCount().values.sum()
        }

    }
}

fun main() {

    val day = "Day20"
    val testInput = readInput("${day}_test")
    check(Day20.part1(testInput, 1).size.also(::println) == Day20.TEST1)
    check(
        Day20.part2(testInput, 50)
                .also(::println) == Day20.TEST2
    )

    val input = readInput(day)
    measureTime {
        Day20.part1(input, 100).values.sum()
                .also(::println)
                .also { check(it == 1406) }
    }.also { println("Part1 took $it") }

    measureTime {
        Day20.part2(input, 100)
                .also(::println)
                .also { check(it == 1006101) }
    }.also { println("Part2 took $it") }
}