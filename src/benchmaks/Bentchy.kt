package benchmaks

import Day21
import kotlinx.benchmark.*
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import readInput

@OutputTimeUnit(BenchmarkTimeUnit.MICROSECONDS)
//@Warmup(iterations = 10, time = 500, timeUnit = BenchmarkTimeUnit.MILLISECONDS)
//@Measurement(iterations = 20, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
class Bentchy {

    val day = "Day21"
    lateinit var input: List<String>

    @Setup
    fun setup() {
        input = readInput(day)
    }

    @Benchmark
    fun part1(): Boolean {
        return Day21.part1(input) == 197560L
    }

}