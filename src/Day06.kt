enum class Direction(c: Char) {
    NORTH('^') {
        override fun move(i: Int, j: Int) = Pair(i - 1, j)
        override fun rotateRight() = EAST
    },
    SOUTH('v') {
        override fun move(i: Int, j: Int) = Pair(i + 1, j)
        override fun rotateRight() = WEST
    },
    WEST('>') {
        override fun move(i: Int, j: Int) = Pair(i, j - 1)
        override fun rotateRight() = NORTH
    },
    EAST('<') {
        override fun move(i: Int, j: Int) = Pair(i, j + 1)
        override fun rotateRight() = SOUTH
    };

    open fun move(i: Int, j: Int) = Pair(i, j)
    open fun rotateRight() = this
}

fun main() {

    val day = "Day06"
    val test1 = 41
    val test2 = 6

    fun parseMap(input: List<String>) = input.map { line -> line.map { it == '#' } }

    fun pathBlocked(map: List<List<Boolean>>, index: Pair<Int, Int>) = map[index.first][index.second]

    fun findStart(input: List<String>): Pair<Pair<Int, Int>, Direction> {
        input.forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                if (c in arrayOf('<', '^', '>', 'v')) {
                    return Pair(
                        Pair(i, j), when (c) {
                            // TODO - there should be a better way for this
                            '^' -> Direction.NORTH
                            'v' -> Direction.SOUTH
                            '>' -> Direction.WEST
                            '<' -> Direction.EAST
                            else -> Direction.WEST
                        }
                    )
                }
            }
        }
        return Pair(Pair(-1, -1), Direction.NORTH)
    }

    fun isInbound(map: List<List<Boolean>>, position: Pair<Int, Int>) =
        position.first >= 0 && position.second >= 0 &&
                position.first < map.size && position.second < map.first().size

    fun part1(input: List<String>): Pair<Int, Set<Pair<Int, Int>>> {

        val blockedPositions = parseMap(input)
        var (guardPosition, direction) = findStart(input)
        val passedPositions = mutableSetOf(guardPosition)

        while (isInbound(blockedPositions, guardPosition)) {

            val nextPosition = direction.move(guardPosition.first, guardPosition.second)

            if (isInbound(blockedPositions, nextPosition) && pathBlocked(blockedPositions, nextPosition)) {
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
            if (isInbound(map, nextPosition) && pathBlocked(map, nextPosition)) {
                direction = direction.rotateRight()
            } else {
                guardPosition = nextPosition
            }
        }
        return false
    }

    fun part2(input: List<String>, passedPosition: Set<Pair<Int, Int>>): Int {
        val blockedPositions = parseMap(input)
        val (guardStartPosition, direction) = findStart(input)

        return passedPosition
            .filter { it != guardStartPosition }
            .map {
                val newBlocker = blockedPositions.map { a -> a.toMutableList() }
                newBlocker[it.first][it.second] = true
                newBlocker
            }
            .count { isInALoop(guardStartPosition, direction, it) }
    }

    val testInput = readInput("${day}_test")
    val (test1Result, test1Passed) = part1(testInput)
    check(test1Result.also(::println) == test1)
    check(part2(testInput, test1Passed).also(::println) == test2)

    val input = readInput(day)
    val (part1Result, passedPositions) = part1(input) // 4903
    part1Result.also { check(it == 4903) }.println()
    part2(input, passedPositions).println() // 1911
}
