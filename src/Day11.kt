import java.util.*
import kotlin.math.log10
import kotlin.math.pow
import kotlin.time.measureTime

typealias StoneData = Pair<Long, Int>
typealias StonePile = ArrayDeque<StoneData>

class Day11 {
    companion object {
        const val TEST1 = 55312L

        private fun parseInput(input: List<String>, blinks: Int): StonePile = StonePile().let { q ->
            input.first().split(' ').forEach { c -> q.add(c.toLong() to blinks) }
            q
        }

        private val cache = emptyMap<StoneData, StonePile>().toMutableMap()
        private fun blink(stone: StoneData): StonePile = cache.getOrPut(stone) {
            val addedStones = StonePile()
            var (nextStone, times) = stone
            repeat(times) { i ->
                val digits = if (nextStone == 0L) 1 else log10(nextStone.toDouble()).toInt() + 1
                nextStone = when {
                    nextStone == 0L -> 1L
                    digits % 2 == 0 -> {
                        val mask = (10.0).pow(digits / 2).toLong()
                        val prefix = nextStone / mask
                        addedStones.add(nextStone - (prefix * mask) to times - i - 1)
                        prefix
                    }

                    else -> nextStone * 2024
                }
            }
            addedStones
        }

        private val secondaryCache = emptyMap<StoneData, Long>().toMutableMap()
        private fun quantityFromStone(stone: StoneData): Long = secondaryCache.getOrPut(stone) {
            val byProduct = blink(stone)
            var thisStoneGeneration = 1L
            while (byProduct.isNotEmpty()) {
                val nextStone = byProduct.removeFirst()
                thisStoneGeneration += if (secondaryCache.containsKey(nextStone)) {
                    secondaryCache[nextStone]!!
                } else {
                    quantityFromStone(nextStone)
                }
            }
            thisStoneGeneration
        }

        private fun countStones(stones: StonePile): Long {
            var quantity = 0L
            while (stones.isNotEmpty()) {
                quantity += quantityFromStone(stones.removeFirst())
            }
            return quantity
        }

        fun part1(input: List<String>): Long = countStones(parseInput(input, 25))

        fun part2(input: List<String>): Long = countStones(parseInput(input, 75))
    }
}

fun main() {

    val day = "Day11"

    val testInput = readInput("${day}_test")
    check(Day11.part1(testInput).also(::println) == Day11.TEST1)

    val input = readInput(day)
    measureTime {
        Day11.part1(input).also { check(it == 175006L) }.println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day11.part2(input).println()
    }.also { println("Part2 took $it") }
}