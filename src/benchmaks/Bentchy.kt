package benchmaks

import Day09
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

    val day = "Day09"
    lateinit var input: String

    @Setup
    fun setup() {
        input = readInput(day).first()
    }

//    @Benchmark
//    fun oldPart1(): Int {
//        return Day09.oldPart1(input)
//    }

    @Benchmark
    fun part1(): Long {
        return Day09.part1(input)
    }

//    @Benchmark
//    fun part2(): Int {
//        return Day09.part2(input)
//    }
}