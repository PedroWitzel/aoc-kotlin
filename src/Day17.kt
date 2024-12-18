import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.pow
import kotlin.time.measureTime

class Day17 {

    enum class Instruction(val i: Int) {
        adv(0) {
            override fun doIt(operand: Int, memory: CPU) {
                val comboOperand = memory.fromComboOperand(operand)
                // println("$this $operand = ${memory.regA} - combo ope $comboOperand")
                memory.regA = (memory.regA / 2.0.pow(comboOperand.toDouble())).toLong()
                memory.programCounter += 2
            }
        },
        bxl(1) {
            override fun doIt(operand: Int, memory: CPU) {
                // println("$this $operand = ${memory.regB}")
                memory.regB = memory.regB.xor(operand.toLong())
                memory.programCounter += 2
            }
        },
        bst(2) {
            override fun doIt(operand: Int, memory: CPU) {
                val comboOperand = memory.fromComboOperand(operand).toLong()
                // println("$this $operand = ${memory.regB} - combo ope $comboOperand")
                memory.regB = comboOperand.mod(8L)
                memory.programCounter += 2
            }
        },
        jnz(3) {
            override fun doIt(operand: Int, memory: CPU) {
                // println("$this $operand = ${memory.regA}")
                if (memory.regA != 0L) memory.programCounter = operand
                else memory.programCounter += 2
            }
        },
        bxc(4) {
            override fun doIt(operand: Int, memory: CPU) {
                // println("$this $operand = ${memory.regB} - ${memory.regC}")
                memory.regB = memory.regB.xor(memory.regC)
                memory.programCounter += 2
            }
        },
        outy(5) {
            override fun doIt(operand: Int, memory: CPU) {
                memory.outputBuffer.add(memory.fromComboOperand(operand).mod(8))
                memory.programCounter += 2
            }
        },
        bdv(6) {
            override fun doIt(operand: Int, memory: CPU) {
                val comboOperand = memory.fromComboOperand(operand)
                // println("$this $operand = ${memory.regA} - combo ope $comboOperand")
                memory.regB = (memory.regA / 2.0.pow(comboOperand.toDouble())).toLong()
                memory.programCounter += 2
            }
        },
        cdv(7) {
            override fun doIt(operand: Int, memory: CPU) {
                val comboOperand = memory.fromComboOperand(operand)
                // println("$this $operand = ${memory.regA} - combo ope $comboOperand")
                memory.regC = (memory.regA / 2.0.pow(comboOperand.toDouble())).toLong()
                memory.programCounter += 2
            }
        };

        abstract fun doIt(operand: Int, memory: CPU)
    }

    data class CPU(var regA: Long, var regB: Long, var regC: Long, val program: List<Int>) {

        var programCounter = 0
        val outputBuffer = emptyList<Int>().toMutableList()

        override fun toString(): String =
            "regA: $regA, regB: $regB, regC: $regC, prog: $program, PG: $programCounter, out: $outputBuffer"

        fun outputsSelf() = outputBuffer == program

        fun looksGood() = program.slice(outputBuffer.indices) == outputBuffer

        fun fromComboOperand(operand: Int): Long = when (operand) {
            in 0..3 -> operand.toLong()
            4 -> regA
            5 -> regB
            6 -> regC
            else -> error("Shouldn't be here")
        }

        fun next(): Boolean {

            if (programCounter >= program.size) return false

            val instruction = Instruction.entries.find { it.i == program[programCounter] }
            if (instruction == null) return false

            instruction.doIt(program[programCounter + 1], this)

            return true
        }
    }

    companion object {
        const val TEST1 = "4,6,3,5,6,3,5,2,1,0"
        const val TEST2 = 117440L

        fun initCpu(input: List<String>): CPU = CPU(
            regA = input[0].split(":", " ").last().toLong(),
            regB = input[1].split(":", " ").last().toLong(),
            regC = input[2].split(":", " ").last().toLong(),
            program = input.last().split(":", " ").last().split(",").map { it.toInt() })

        fun part1(input: List<String>): String {
            val cpu = initCpu(input)
            while (cpu.next()) {
//                cpu.println()
            }
            return cpu.outputBuffer.joinToString(",")
        }

        fun part2(input: List<String>): Long {
            val originalCpu = initCpu(input)
            var initialA = 8.0.pow(originalCpu.program.size - 1).toLong()
            var finalA = 8.0.pow(originalCpu.program.size).toLong()
            var answer = 0L

//            (initialA..finalA).forEach { regA ->
//                val cpu = originalCpu.copy(regA = regA)
//                while (cpu.next()) {
//                }
//                if (cpu.outputsSelf()) {
//                    println(regA)
//                    return regA
//                }
//            }


            runBlocking(Dispatchers.Default) {
                (initialA..finalA).forEach { regA ->
                    launch {
                        val cpu = originalCpu.copy(regA = regA)
                        while (cpu.next() && cpu.looksGood()) {
                        }
                        if (cpu.outputsSelf()) {
                            println(regA)
                            answer = regA
                        }
                    }
                }
            }
            return answer
        }
    }
}

fun main() {

    val day = "Day17"

//    val testInput = readInput("${day}_test")
//    check(Day17.part1(testInput).also(::println) == Day17.TEST1)
//    val test2Input = readInput("${day}_test2")
//    check(Day17.part2(test2Input).also(::println) == Day17.TEST2)

    val input = readInput(day)

    measureTime {
        Day17.part1(input).also { it == "2,0,7,3,0,3,1,3,7" }.println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day17.part2(input).println()
    }.also { println("Part2 took $it") }
}