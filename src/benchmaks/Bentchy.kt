package benchmaks

import Day14
import Position
import kotlinx.benchmark.*
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import readInput

@OutputTimeUnit(BenchmarkTimeUnit.MILLISECONDS)
//@Warmup(iterations = 10, time = 500, timeUnit = BenchmarkTimeUnit.MILLISECONDS)
//@Measurement(iterations = 20, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
class Bentchy {

    val day = "Day14"
    lateinit var input: List<String>

    @Setup
    fun setup() {
        input = readInput(day)
    }

//    @Benchmark
//    fun oldPart1(): Int {
//        return Day11.oldPart1(input)
//    }

    @Benchmark
    fun part1(): Int {
        val mapSize = Position(101, 103)
        return Day14.part2(input, mapSize)
    }

//    @Benchmark
//    fun part2(): Int {
//        val (_, passedPositions) = Day11.part1(input) // 4903
//        return Day11.part2(input, passedPositions) //667 -> 419
//    }
}