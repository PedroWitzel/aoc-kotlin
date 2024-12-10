package benchmaks

import Day10
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

    val day = "Day10"
    lateinit var input: List<String>

    @Setup
    fun setup() {
        input = readInput(day)
    }

//    @Benchmark
//    fun oldPart1(): Int {
//        return Day10.oldPart1(input)
//    }

    @Benchmark
    fun part1(): Int {
        // 1st: 2039
        // 2nd(sequence algo): 1609
        // 3rd(starts on init): 1475
        return Day10.part1(input)
    }

//    @Benchmark
//    fun part2(): Int {
//        val (_, passedPositions) = Day10.part1(input) // 4903
//        return Day10.part2(input, passedPositions) //667 -> 419
//    }
}