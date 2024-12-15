import kotlin.time.measureTime

class Day14 {

    class Robot(input: String) {

        var position: Position
        val speed: Position

        init {
            val x = input.split('=', ',', ' ')
            position = Position(x[1].toInt(), x[2].toInt())
            speed = Position(x[4].toInt(), x[5].toInt())
        }

        fun step(border: Position) {
            position = wrapAt(position.x + speed.x, position.y + speed.y, border)
        }

        fun wrapAt(x: Int, y: Int, border: Position): Position {
            val x = x % border.x
            val y = y % border.y
            return Position(
                if (x < 0) border.x + x else x,
                if (y < 0) border.y + y else y
            )
        }

        fun walk(seconds: Int, border: Position): Robot {
            position = (position + speed * seconds).wrapAt(border)
            return this
        }

        fun Position.wrapAt(border: Position): Position = (this % border).let {
            Position(
                if (it.x < 0) border.x + it.x else it.x,
                if (it.y < 0) border.y + it.y else it.y
            )
        }

        fun debug(border: Position) {
            val midPoint = border / 2
            val wrap = position.wrapAt(border)
            wrap.debug()
            wrap.quadrant(border).debug()
            repeat(border.x) { x ->
                repeat(border.y) { y ->
                    if (Position(x, y) == wrap) print('X') else {
                        if (x == midPoint.x) print('-')
                        else if (y == midPoint.y) print('|')
                        else print('.')
                    }
                }
                println("")
            }
            println("")
        }
    }

    companion object {
        const val TEST1 = 12

        fun part1(input: List<String>, mapSize: Position): Int {
            val walkTime = 100
            return input
                .asSequence()
                .map { Robot(it) }
                .map { it.walk(walkTime, mapSize) }
                .map { it.position.quadrant(mapSize) }
                .filter { it != 0 }
                .groupBy { it }
                .values
                .map { it.size }
                .fold(1) { acc, it -> acc * it }
        }

        fun areOnDifferentPlaces(robos: List<Robot>): Boolean {
            val testSet = emptySet<Position>().toMutableSet()
            robos.forEach { robot -> if (testSet.add(robot.position) == false) return false }
            return true
        }

        fun part2(input: List<String>, mapSize: Position): Int {

            val robots = input.map { Robot(it) }
            var walked = 0

            while (!areOnDifferentPlaces(robots)) {
                ++walked
                robots.forEach {
                    it.step(mapSize)
                }
            }

            return walked
        }
    }
}

fun main() {

    val day = "Day14"
    var mapSize = Position(11, 7)

    val testInput = readInput("${day}_test")
    check(Day14.part1(testInput, mapSize).also(::println) == Day14.TEST1)

    mapSize = Position(101, 103)
    val input = readInput(day)

    measureTime {
        Day14.part1(input, mapSize).also { check(it == 214109808) }.println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day14.part2(input, mapSize).println()
    }.also { println("Part2 took $it") }
}