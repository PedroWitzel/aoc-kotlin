import kotlin.time.measureTime

class Day22 {

    companion object {
        const val TEST1 = 37327623L
        const val TEST2 = 23L

        inline fun Long.mix(secret: Long) = this.xor(secret)
        inline fun Long.prune() = this.mod(16777216L)
        inline fun Long.nextSecret(): Long {
            val first = this.shl(6)
                    .mix(this)
                    .prune()
            val second = first.shr(5)
                    .mix(first)
                    .prune()
            return second.shl(11)
                    .mix(second)
                    .prune()
        }

        fun part1(input: List<Long>): Long = input.sumOf {
            var secret = it
            repeat(2000) {
                secret = secret.nextSecret()
            }
            secret
        }

        private fun Long.secretsOf() = buildList {
            var secret = this@secretsOf
            add(secret)
            repeat(2000) {
                secret = secret.nextSecret()
                add(secret)
            }
        }.asSequence()

        fun Long.leastSignificantDigit() = mod(10L)

        private fun mostBananas(code: String, secrets: Map<String, Long>) = secrets.getOrDefault(code, 0)

        fun part2(input: List<Long>): Long {

            val allSecrets = input.map {

                // Map of quantity to diff with previous
                val secrets = it.secretsOf()
                        .zipWithNext { a, b ->
                            b.leastSignificantDigit() to b.leastSignificantDigit() - a.leastSignificantDigit()
                        }
                        .toList()

                // Map of quantity to the code that the monkey gets it
                val diffList = buildList {
                    (3..<secrets.size).forEach { i ->
                        add(secrets[i].first to "${secrets[i].second},${secrets[i - 1].second},${secrets[i - 2].second},${secrets[i - 3].second}")
                    }
                }

                // Found the max of bananas that this secret produces
                val max = diffList.maxOf { it.first }

                // returns the diff map and all the codes that would yield max of bananas
                diffList to diffList.mapNotNull { if (it.first == max) it.second else null }
            }

            val allCodes = allSecrets.flatMap { it.second }
                    .toSet()

            val reversedSecrets = allSecrets.map {
                buildMap {
                    it.first.forEach { (banana, code) ->
                        if (!this.containsKey(code)) put(code, banana)
                    }
                }
            }

            return allCodes
                    .maxOf { code ->
                        // Run all found codes through all possible secrets to find the right combination
                        reversedSecrets.sumOf { mostBananas(code, it) }
                    }
        }
    }
}

fun main() {

    val day = "Day22"

    val testInput = readInput("${day}_test").map(String::toLong)
    check(
        Day22.part1(testInput)
                .also(::println) == Day22.TEST1
    )
    val test2Input = readInput("${day}_test2").map(String::toLong)
    check(
        Day22.part2(test2Input)
                .also(::println) == Day22.TEST2
    )

    val input = readInput(day).map(String::toLong)

    measureTime {
        Day22.part1(input)
                .also { check(it == 20071921341L) }
                .println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day22.part2(input)
                .also { check(it == 2242L) }
                .println()
    }.also { println("Part2 took $it") }
}