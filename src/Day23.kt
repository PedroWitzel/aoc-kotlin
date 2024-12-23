import kotlin.time.measureTime

class Day23 {

    companion object {
        const val TEST1 = 7
        const val TEST2 = "co,de,ka,ta"

        fun parseInput(input: List<String>): Map<String, List<String>> = buildMap<String, MutableList<String>> {
            input.map { it.split("-") }
                .forEach { (a, b) ->
                    getOrPut(a) { mutableListOf() }.add(b)
                    getOrPut(b) { mutableListOf() }.add(a)
                }
        }

        fun part1(input: List<String>): Int {

            val connections = parseInput(input)
            val networks = buildSet<List<String>> {
                connections.forEach { first, deps ->
                    deps.forEach { second ->
                        connections[second]?.filter { it != first && deps.contains(it) }
                            ?.forEach { third ->
                                add(
                                    listOf(first, second, third).sorted()
                                )
                            }
                    }
                }
            }

            return networks.filter { n -> n.any { it.startsWith('t') } }.size
        }

        private fun addPcToNetwork(
            pc: Pair<String, List<String>>,
            network: MutableSet<String>,
            connections: Map<String, List<String>>
        ) {
            network.add(pc.first)
            pc.second
                .forEach { nextPc ->
                    val nextDeps = connections[nextPc]!!
                    if (network.intersect(nextDeps).size == network.size) {
                        addPcToNetwork(Pair(nextPc, nextDeps), network, connections)
                    }
                }
        }

        fun part2(input: List<String>): String {
            val connections = parseInput(input).toMutableMap()
            var biggestLan = emptySet<String>()

            connections.entries.forEach {
                var lan = emptySet<String>().toMutableSet()
                addPcToNetwork(it.toPair(), lan, connections)
                if (lan.size > biggestLan.size) {
                    biggestLan = lan
                }
            }

            return biggestLan.sorted()
                .joinToString(",")
        }
    }
}

fun main() {

    val day = "Day23"

    val testInput = readInput("${day}_test")
    check(
        Day23.part1(testInput)
            .also(::println) == Day23.TEST1
    )
    check(
        Day23.part2(testInput)
            .also(::println) == Day23.TEST2
    )

    val input = readInput(day)

    measureTime {
        Day23.part1(input)
            .also { check(it == 1411) }
            .println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day23.part2(input)
            .println()
    }.also { println("Part2 took $it") }
}
