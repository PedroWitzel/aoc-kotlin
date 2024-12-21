import Direction.*
import kotlin.time.measureTime

class Day21 {

    data class Pad(val entry: String, val keys: Map<Char, Position>) {

        override fun toString(): String = entry

        private val keyPositions = keys.entries.associate { (key, pos) -> pos to key }

        private fun Position.nextPositionTo(direction: Direction, map: Set<Position>) =
            walkTo(direction).let { nextPosition ->
                if (map.contains(nextPosition)) nextPosition else null
            }

        private fun Position.step(towards: Position, map: Set<Position>): List<Position> {
            val (x, y) = towards - this
            return buildList {
                if (x < 0) add(nextPositionTo(NORTH, map))
                if (x > 0) add(nextPositionTo(SOUTH, map))
                if (y < 0) add(nextPositionTo(WEST, map))
                if (y > 0) add(nextPositionTo(EAST, map))
            }.filterNotNull()
        }

        private fun Position.goTo(target: Position, map: Set<Position>): List<List<Position>> {

            // from the origin point
            val possibleNext = step(target, map).let { listOf(this) to it }
            val toAnalyse = listOf(possibleNext).toMutableList()
            val pathsToFinish = emptyList<List<Position>>().toMutableList()

            while (toAnalyse.isNotEmpty()) {
                val path = toAnalyse.removeFirst()
                if (path.first.last() == target) {
                    pathsToFinish.add(path.first)
                } else {
                    path.second.forEach { position ->
                        toAnalyse.add(
                            position.step(target, map)
                                    .let { path.first + position to it })
                    }
                }
            }
            return pathsToFinish
        }

        companion object {
            val fromToCache = mutableMapOf<Triple<Position, Position, Long>, Long>()
        }

        fun toKeypad(depth: Long, nextKeys: Map<Char, Position>): Long {
            var pos = keys['A']!!
            var steps = 0L
            entry.forEach { ch ->
                val target = keys[ch] ?: error("Invalid char $ch")
                fromToCache.getOrPut(Triple(pos, target, depth)) {
                    pos.goTo(target, keyPositions.keys)
                            .asSequence()
                            .map {
                                it.zipWithNext { origin, dest ->
                                    val (x, y) = dest - origin
                                    if (x < 0) '^'
                                    else if (y > 0) '>'
                                    else if (x > 0) 'v'
                                    else if (y < 0) '<'
                                    else error("humm...")
                                }
                            }
                            .map { path ->
                                if (depth > 0)
                                    Pad(path.joinToString("") + "A", nextKeys).toKeypad(depth - 1, nextKeys)
                                else path.size.toLong() + 1L
                            }
                            .minOf { it }
                }
                        .let { steps += it }

                pos = target
            }

            return steps
        }
    }

    companion object {
        const val TEST1 = 126_384L

        val keypad = buildMap<Char, Position> {
            put('A', Position(0, 2))
            put('^', Position(0, 1))
            put('<', Position(1, 0))
            put('v', Position(1, 1))
            put('>', Position(1, 2))
        }

        val numpad = buildMap<Char, Position> {
            put('A', Position(3, 2))
            put('0', Position(3, 1))
            put('1', Position(2, 0))
            put('2', Position(2, 1))
            put('3', Position(2, 2))
            put('4', Position(1, 0))
            put('5', Position(1, 1))
            put('6', Position(1, 2))
            put('7', Position(0, 0))
            put('8', Position(0, 1))
            put('9', Position(0, 2))
        }

        private fun String.humanInput(depth: Long) = Pad(this, numpad).toKeypad(depth - 1, keypad)

        fun part1(input: List<String>): Long = input.map {
            it.humanInput(3) to it.substringBefore('A')
                    .toInt()
        }
                .sumOf { it.first * it.second }

        fun part2(input: List<String>): Long = input.map {
            it.humanInput(26) to it.substringBefore('A')
                    .toInt()
        }
                .sumOf { it.first * it.second }
    }
}

fun main() {

    val day = "Day21"

    val testInput = readInput("${day}_test")
    check(
        Day21.part1(testInput)
                .also(::println) == Day21.TEST1
    )

    val input = readInput(day)

    measureTime {
        Day21.part1(input)
                .println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day21.part2(input)
                .println()
    }.also { println("Part2 took $it") }
}