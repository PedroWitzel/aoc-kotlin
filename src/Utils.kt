import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()
fun readInputAsLine(name: String) = Path("src/$name.txt").readText().trim()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
fun Any?.debug() = this.println().let { this@debug }

data class Position(val x: Int, val y: Int) {

    operator fun minus(other: Position) = Position(
        this.x - other.x, this.y - other.y
    )

    operator fun plus(other: Position) = Position(
        this.x + other.x, this.y + other.y
    )

    fun mul(times: Int) = Position(
        this.x * times, this.y * times
    )

    operator fun div(other: Int) = Position(
        this.x / other, this.y / other
    )

    operator fun rem(other: Position) = Position(
        this.x % other.x, this.y % other.y
    )

    fun mirrorPositionFrom(other: Position) = (other - this).let { other + it }

    fun isNeighborsWith(other: Position) = (this - other).let { it.x in -1..1 && it.y in -1..1 }

    fun neighbors(border: Position): Set<Position> = buildSet {
        if (x > 0) add(Position(x - 1, y))
        if (x < border.x) add(Position(x + 1, y))
        if (y > 0) add(Position(x, y - 1))
        if (y < border.y) add(Position(x, y + 1))
    }

    fun right() = Position(x + 1, y)
    fun left() = Position(x - 1, y)
    fun up() = Position(x, y - 1)
    fun down() = Position(x, y + 1)

    fun quadrant(border: Position): Int {
        val midPoint = border / 2
        return when {
            x < midPoint.x && y < midPoint.y -> 1
            x < midPoint.x && y > midPoint.y  -> 2
            x > midPoint.x  && y < midPoint.y -> 3
            x > midPoint.x  && y > midPoint.y  -> 4
            else -> 0
        }
    }
}
