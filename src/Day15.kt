import Direction.EAST
import Direction.WEST
import kotlin.time.measureTime

class Day15 {

    abstract class Thingy {
        abstract var position: Position
        abstract fun canMoveTo(direction: Direction, map: Set<Thingy>): Triple<Boolean, Position, List<Thingy>>
        abstract fun moveTo(direction: Direction, map: Set<Thingy>)

        fun value() = 100 * position.x + position.y - 1
    }

    class Wall(override var position: Position) : Thingy() {
        override fun canMoveTo(direction: Direction, map: Set<Thingy>) =
            Triple(false, Position(0, 0), emptyList<Thingy>())

        override fun moveTo(direction: Direction, map: Set<Thingy>) {}
    }

    class Box(override var position: Position) : Thingy() {
        override fun canMoveTo(direction: Direction, map: Set<Thingy>): Triple<Boolean, Position, List<Thingy>> {
            val nextPos = position.walkTo(direction)
            val westPos = nextPos.walkTo(WEST)
            val eastPos = nextPos.walkTo(EAST)
            val collidingBlocks = map.filter {
                it.position != position &&
                        (it.position == nextPos || it.position == eastPos ||
                                (westPos != position && it.position == westPos)
                                )
            }

            val movableBoxes = collidingBlocks.toMutableList()
            val canMove = collidingBlocks.isEmpty() || collidingBlocks.all {
                val (canMove, _, theirPoints) = it.canMoveTo(direction, map)
                movableBoxes.addAll(theirPoints)
                canMove
            }
            return Triple(canMove, nextPos, movableBoxes)
        }

        override fun moveTo(direction: Direction, map: Set<Thingy>) {
            position = position.walkTo(direction)
        }

    }

    class Robot(override var position: Position) : Thingy() {
        override fun canMoveTo(direction: Direction, map: Set<Thingy>): Triple<Boolean, Position, List<Thingy>> {
            val nextPos = position.walkTo(direction)
            val eastPos = nextPos.walkTo(EAST)
            val collidingBlocks = map.filter {
                it.position == nextPos || it.position == eastPos
            }
            val movableBoxes = collidingBlocks.toMutableList()
            val canMove = collidingBlocks.isEmpty() || collidingBlocks.all {
                val (canMove, _, theirPoints) = it.canMoveTo(direction, map)
                movableBoxes.addAll(theirPoints)
                canMove
            }
            return Triple(canMove, nextPos, movableBoxes)
        }

        override fun moveTo(direction: Direction, map: Set<Thingy>) {
            val (canMove, nextPos, collidingBlocks) = canMoveTo(direction, map)
            if (canMove) {
                collidingBlocks.forEach { it.moveTo(direction, map) }
                position = nextPos
            }
        }
    }

    companion object {
        const val TEST1 = 10092
        const val TEST2 = 9021
        var scale = 1

        private fun parseInput(input: List<String>): Pair<MutableSet<Thingy>, List<Direction>> {
            val (map, inputs) = input.partition { it.contains("#") }

            fun shift(x: Int) = ((x + 1) * scale) - 1
            return buildSet {
                map.forEachIndexed { i, line ->
                    line.forEachIndexed { j, char ->
                        when (char) {
                            '#' -> add(Wall(Position(i, shift(j))))
                            'O' -> add(Box(Position(i, shift(j))))
                            '@' -> add(Robot(Position(i, shift(j) - 1)))
                            else -> {}
                        }
                    }
                }
            }.toMutableSet() to inputs.joinToString("").mapNotNull {
                Direction.entries.find { d -> d.c == it }
            }
        }

        fun draw(map: Set<Thingy>) {
            val fast = map.groupBy { it.position }
            repeat(10) { i ->
                repeat(20) { j ->
                    val thingies = fast[Position(i, j)]?.first()
                    when (thingies) {
                        is Robot -> print("@")
                        is Box -> print("0")
                        is Wall -> print("#")
                        else -> print(".")
                    }
                }
                println("")
            }
            println("")
            println("")
        }

        fun part1(input: List<String>): Int {

            val (thingies, inputs) = parseInput(input)
            val robot = thingies.find { it is Robot }!!
            thingies.remove(robot)

            inputs.forEach { move ->
                robot.moveTo(move, thingies)
            }

            return thingies.filter { it is Box }.sumOf { it.value() }
        }
    }
}

fun main() {

    val day = "Day15"

    val testInput = readInput("${day}_test")
//    check(Day15.part1(testInput).also(::println) == Day15.TEST1)

    Day15.scale = 2
    check(Day15.part1(testInput).also(::println) == Day15.TEST2)

    val input = readInput(day)

    Day15.scale = 1
//    measureTime {
//        Day15.part1(input).also { check(it == 1563092) }.println()
//    }.also { println("Part1 took $it") }


    Day15.scale = 2
    measureTime {
        Day15.part1(input).println()
    }.also { println("Part2 took $it") }
}