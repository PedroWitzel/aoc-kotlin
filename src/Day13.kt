import kotlinx.benchmark.isApproximateZero
import kotlin.math.absoluteValue
import kotlin.math.roundToLong
import kotlin.time.measureTime

class Day13 {

    data class Location(val x: Double, val y: Double)
    data class Increments(val x: Double, val y: Double)

    class Prize(input: String) {

        val aInc: Increments
        val bInc: Increments
        var prizeLocation: Location

        init {
            val sums = input.split("+")
            aInc = Increments(sums[1].substring(0..1).toDouble(), sums[2].substring(0..1).toDouble())
            bInc = Increments(sums[3].substring(0..1).toDouble(), sums[4].substring(0..1).toDouble())

            prizeLocation = sums.last().split("=").let {
                Location(it[1].split(",").first().toDouble(), it.last().toDouble())
            }
        }

        private fun costOf(pressA: Double, pressB: Double): Long = (pressA * 3 + pressB).toLong()

        fun calculateDirectResult(): Increments? {

            if (bInc.x.isApproximateZero() && aInc.x.isApproximateZero()) return null

            val div = bInc.y * aInc.x - aInc.y * bInc.x
            if (div.absoluteValue.isApproximateZero()) return null

            val A = (bInc.y * prizeLocation.x - bInc.x * prizeLocation.y) / div
            val B = (aInc.x * prizeLocation.y - aInc.y * prizeLocation.x) / div
            if (!isRound(A) || !isRound(B)) return null

            return Increments(A, B)
        }

        private fun isRound(calculatedBx: Double): Boolean =
            calculatedBx.rem(calculatedBx.roundToLong()).absoluteValue.isApproximateZero()

        fun minMax(): Long {
            val result = calculateDirectResult()
            return if (result != null) costOf(result.x, result.y) else 0L
        }

        fun addConversion(): Prize {
            prizeLocation = Location(
                prizeLocation.x + 10000000000000L, prizeLocation.y + 10000000000000L
            )
            return this
        }
    }

    companion object {
        const val TEST1 = 480L

        fun parseInput(input: String) = input.split("\r\n\r\n").map { Prize(it) }

        fun part1(input: String): Long = parseInput(input).sumOf(Prize::minMax)

        fun part2(input: String): Long = parseInput(input).map(Prize::addConversion).sumOf(Prize::minMax)
    }
}

fun main() {

    val day = "Day13"

    val testInput = readInputAsLine("${day}_test")
    check(Day13.part1(testInput).also(::println) == Day13.TEST1)

    val input = readInputAsLine(day)

    measureTime {
        Day13.part1(input).also { check(it == 35997L) }.println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day13.part2(input).also { check(it == 82510994362072L) }.println()
    }.also { println("Part2 took $it") }
}
