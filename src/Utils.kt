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
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16).padStart(32, '0')

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

    operator fun times(times: Int) = Position(
        this.x * times, this.y * times
    )

    operator fun div(other: Int) = Position(
        this.x / other, this.y / other
    )

    operator fun rem(other: Position) = Position(
        this.x % other.x, this.y % other.y
    )

    fun mirrorPositionFrom(other: Position) = (other - this).let { other + it }

    fun neighbors(border: Position): Set<Position> = buildSet {
        if (x > 0) add(Position(x - 1, y))
        if (x < border.x) add(Position(x + 1, y))
        if (y > 0) add(Position(x, y - 1))
        if (y < border.y) add(Position(x, y + 1))
    }

    @Deprecated(message = "Worked for Day12, but the senses are wrong")
    fun right() = Position(x + 1, y)

    @Deprecated(message = "Worked for Day12, but the senses are wrong")
    fun left() = Position(x - 1, y)

    @Deprecated(message = "Worked for Day12, but the senses are wrong")
    fun up() = Position(x, y - 1)

    @Deprecated(message = "Worked for Day12, but the senses are wrong")
    fun down() = Position(x, y + 1)

    fun walkTo(direction: Direction): Position = direction.move(this)

    fun quadrant(border: Position): Int {
        val midPoint = border / 2
        return when {
            x < midPoint.x && y < midPoint.y -> 1
            x < midPoint.x && y > midPoint.y -> 2
            x > midPoint.x && y < midPoint.y -> 3
            x > midPoint.x && y > midPoint.y -> 4
            else -> 0
        }
    }
}

enum class Direction(val c: Char) {
    NORTH('^') {
        override fun move(i: Int, j: Int) = Pair(i - 1, j)
        override fun move(position: Position) = Position(position.x - 1, position.y)
        override fun rotateRight() = EAST
    },
    SOUTH('v') {
        override fun move(i: Int, j: Int) = Pair(i + 1, j)
        override fun move(position: Position) = Position(position.x + 1, position.y)
        override fun rotateRight() = WEST
    },
    WEST('<') {
        override fun move(i: Int, j: Int) = Pair(i, j - 1)
        override fun move(position: Position) = Position(position.x, position.y - 1)
        override fun rotateRight() = NORTH
    },
    EAST('>') {
        override fun move(i: Int, j: Int) = Pair(i, j + 1)
        override fun move(position: Position) = Position(position.x, position.y + 1)
        override fun rotateRight() = SOUTH
    };

    open fun move(i: Int, j: Int) = Pair(i, j)
    open fun move(position: Position) = position
    open fun rotateRight() = this
}