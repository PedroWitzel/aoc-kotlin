import kotlin.math.min

fun main() {

    val day = "Day03"
    val test1 = 161
    val test2 = 48

    val mulRegex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()

    fun multiplyString(line: String, start: Int): Pair<Int, Int> =
        mulRegex.find(line.substring(start, min(start + 12, line.length)))
            .let { groups ->
                if (groups != null)
                    Pair(
                        groups.groupValues[1].toInt() * groups.groupValues.last().toInt(),
                        groups.groupValues.first().length
                    )
                else Pair(0, 0)
            }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            mulRegex.findAll(line)
                .sumOf {
                    it.groupValues[1].toInt() * it.groupValues.last().toInt()
                }
        }
    }

    fun part2(input: List<String>): Int {
        var doIt = true
        return input.sumOf { line ->
            var index = 0
            var acc = 0
            while (index < line.length) {
                val c = line[index]
                when (c) {
                    'm' -> {
                        if (doIt) {
                            val (value, length) = multiplyString(line, index)
                            index += if (length == 0) 1 else length
                            acc += value
                        } else ++index
                    }

                    'd' -> {
                        val substring = line.substring(index)
                        if (substring.startsWith("do()")) {
                            doIt = true
                            index += 4
                        } else if (substring.startsWith("don't()")) {
                            doIt = false
                            index += 6
                        } else ++index
                    }

                    else -> ++index
                }
            }
            acc
        }.also(::println)
    }

    val testInput = readInput("${day}_test")
    check(part1(testInput) == test1)
    check(part2(testInput) == test2)

    val input = readInput(day)
    part1(input).println() // 183669043
    part2(input).println() //
}
