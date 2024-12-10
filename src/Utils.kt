import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

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
fun Any?.debug() = println(this).let { this }

data class Position(val x: Int, val y: Int) {

    operator fun minus(other: Position) = Position(
        this.x - other.x, this.y - other.y
    )

    operator fun plus(other: Position) = Position(
        this.x + other.x, this.y + other.y
    )

    fun mirrorPositionFrom(other: Position) = (other - this).let { other + it }

    fun neighbors(border: Position): Set<Position> = buildSet {
        if (x > 0) add(Position(x - 1, y))
        if (x < border.x) add(Position(x + 1, y))
        if (y > 0) add(Position(x, y - 1))
        if (y < border.y) add(Position(x, y + 1))
    }
}