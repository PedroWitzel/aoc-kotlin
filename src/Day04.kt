import kotlin.math.abs

fun main() {

    val day = "Day04"
    val test1 = 18
    val test2 = 9

    fun xmasValidIndices(i: Int, j: Int, iMax: Int, jMax: Int) = listOf(
        Pair(i + 3, j), // forward
        Pair(i, j + 3), // downwards
        Pair(i + 3, j + 3), // ./
        Pair(i + 3, j - 3), // .\
    ).filter { (i, j) -> j >= 0 && i < iMax && j < jMax }


    fun Int.nextTo(destination: Int): Int {
        if (this == destination) {
            return this
        }

        val step = destination - this
        return this + (step / abs(step))
    }

    fun isXmas(maybeXmas: String) = maybeXmas == "XMAS" || maybeXmas == "SAMX"


    fun part1(input: List<String>): Int {

        val charMatrix = input.map(String::toCharArray)

        val lines = input.size
        val columns = charMatrix.first().size
        var xmasFound = 0

        for (i in input.indices) {
            for (j in 0..<columns) {

                if (charMatrix[i][j] !in arrayOf('X', 'S')) continue

                xmasFound += xmasValidIndices(i, j, lines, columns).map { (k, l) ->
                    var maybeXmas = ""
                    var (a, b) = Pair(i, j)
                    repeat(4) {
                        maybeXmas += charMatrix[a][b]
                        a = a.nextTo(k)
                        b = b.nextTo(l)
                    }
                    maybeXmas
                }.count { it.length == 4 && isXmas(it) }

            }
        }

        return xmasFound.also(::println)
    }

    fun masValidIndices(i: Int, j: Int) = listOf(
        Pair(i - 1, j - 1),
        Pair(i + 1, j - 1),
        Pair(i - 1, j + 1),
        Pair(i + 1, j + 1),
    )

    fun part2(input: List<String>): Int {

        val charMatrix = input.map(String::toCharArray)

        val lines = input.size
        val columns = charMatrix.first().size
        var masFound = 0

        for (i in 1..(lines - 2)) {
            for (j in 1..(columns - 2)) {

                if (charMatrix[i][j] != 'A') continue

                masFound += masValidIndices(i, j)
                    .map { (k, l) -> charMatrix[k][l] }
                    .let { maybeMas ->
                        if (maybeMas.joinToString("") in listOf("MMSS", "SMSM", "MSMS", "SSMM")) 1 else 0
                    }
            }
        }

        return masFound.also(::println)
    }

    val testInput = readInput("${day}_test")
    check(part1(testInput) == test1)
    check(part2(testInput) == test2)

    val input = readInput(day)
    part1(input).println() // 2599
    part2(input).println() // < 2001
}
