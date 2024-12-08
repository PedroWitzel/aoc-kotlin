package benchmaks

import Day08
import kotlinx.benchmark.Scope
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import readInput

@State(Scope.Benchmark)
class Bentchy {

    val day = "Day08"
    lateinit var input: List<String>

    @Setup
    fun setup() {
        input = readInput(day)
    }

    @Benchmark
    fun part1(): Int {
        return Day08.part1(input)
    }

    @Benchmark
    fun part2(): Int {
        return Day08.part2(input)
    }
}