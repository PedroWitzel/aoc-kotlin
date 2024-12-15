import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.measureTime

class Day06 {
    companion object {
        val test1 = 41
        val test2 = 6

        fun obstaclesMap(input: List<String>) = input.map { line -> line.map { it == '#' } }

        fun isPathBlocked(map: List<List<Boolean>>, index: Pair<Int, Int>) = map[index.first][index.second]

        fun findStart(input: List<String>): Pair<Pair<Int, Int>, Direction> {
            input.forEachIndexed { i, line ->
                line.indexOfFirst { it == '^' }.also { j ->
                    if (j != -1) return Pair(Pair(i, j), Direction.entries.first { it.c == line[j] })
                }
            }
            error("Input has always a starting point")
        }

        fun isInbound(map: List<List<Boolean>>, position: Pair<Int, Int>) =
            position.first in 0..<map.size && position.second in 0..<map.size

        fun part1(input: List<String>): Pair<Int, Set<Pair<Int, Int>>> {

            val blockedPositions = obstaclesMap(input)
            var (guardPosition, direction) = findStart(input)
            val passedPositions = mutableSetOf(guardPosition)

            while (isInbound(blockedPositions, guardPosition)) {

                val nextPosition = direction.move(guardPosition.first, guardPosition.second)

                if (isInbound(blockedPositions, nextPosition) && isPathBlocked(blockedPositions, nextPosition)) {
                    direction = direction.rotateRight()
                } else {
                    guardPosition = nextPosition
                    if (isInbound(blockedPositions, guardPosition)) passedPositions.add(guardPosition)
                }
            }

            return Pair(passedPositions.size, passedPositions)
        }

        fun isInALoop(startPosition: Pair<Int, Int>, startDirection: Direction, map: List<List<Boolean>>): Boolean {

            var direction = startDirection
            var guardPosition = startPosition
            val visitedPositions = emptySet<Triple<Int, Int, Direction>>().toMutableSet()
            while (isInbound(map, guardPosition)) {

                if (!visitedPositions.add(Triple(guardPosition.first, guardPosition.second, direction))) return true

                val nextPosition = direction.move(guardPosition.first, guardPosition.second)
                if (isInbound(map, nextPosition) && isPathBlocked(map, nextPosition)) {
                    direction = direction.rotateRight()
                } else {
                    guardPosition = nextPosition
                }
            }
            return false
        }

        fun part2(input: List<String>, passedPosition: Set<Pair<Int, Int>>): Int {
            val blockedPositions = obstaclesMap(input)
            val (guardStartPosition, direction) = findStart(input)

            val allPossibilities = passedPosition.asSequence().filter { it != guardStartPosition }.map {
                val newBlocker = blockedPositions.map { a -> a.toMutableList() }
                newBlocker[it.first][it.second] = true
                newBlocker
            }

            val count = AtomicInteger(0)
            runBlocking(Dispatchers.Default) {
                allPossibilities.forEach { map ->
                    launch {
                        isInALoop(guardStartPosition, direction, map).let {
                            if (it) count.incrementAndGet()
                        }
                    }
                }
            }

            return count.get()
        }
    }
}

fun main() {

    val day = "Day06"
    val testInput = readInput("${day}_test")
    val (test1Result, test1Passed) = Day06.part1(testInput)
    check(test1Result.also(::println) == Day06.test1)
    check(Day06.part2(testInput, test1Passed).also(::println) == Day06.test2)

    val input = readInput(day)

    measureTime {
        val (part1Result, passedPositions) = Day06.part1(input) // 4903
        part1Result.also { check(it == 4903) }.println()
        Day06.part2(input, passedPositions).println() // 1911
    }.also(::println)
}