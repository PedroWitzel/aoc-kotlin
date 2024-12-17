import kotlin.math.pow
import kotlin.time.measureTime

class Day17 {

    enum class Instruction(val i: Int) {
        adv(0) {
            override fun doIt(operand: Int, memory: CPU) {
                val comboOperand = memory.fromComboOperand(operand)
                println("$this $operand = ${memory.regA} - combo ope $comboOperand")
                memory.regA = (memory.regA / 2.0.pow(comboOperand)).toInt()
                memory.programCounter += 2
            }
        },
        bxl(1) {
            override fun doIt(operand: Int, memory: CPU) {
                println("$this $operand = ${memory.regB}")
                memory.regB = memory.regB.xor(operand)
                memory.programCounter += 2
            }
        },
        bst(2) {
            override fun doIt(operand: Int, memory: CPU) {
                val comboOperand = memory.fromComboOperand(operand)
                println("$this $operand = ${memory.regB} - combo ope $comboOperand")
                memory.regB = comboOperand.mod(8)
                memory.programCounter += 2
            }
        },
        jnz(3) {
            override fun doIt(operand: Int, memory: CPU) {
                println("$this $operand = ${memory.regA}")
                if (memory.regA != 0) memory.programCounter = operand
                else memory.programCounter += 2
            }
        },
        bxc(4) {
            override fun doIt(operand: Int, memory: CPU) {
                println("$this $operand = ${memory.regB} - ${memory.regC}")
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
                println("$this $operand = ${memory.regA} - combo ope $comboOperand")
                memory.regB = (memory.regA / 2.0.pow(comboOperand)).toInt()
                memory.programCounter += 2
            }
        },
        cdv(7) {
            override fun doIt(operand: Int, memory: CPU) {
                val comboOperand = memory.fromComboOperand(operand)
                println("$this $operand = ${memory.regA} - combo ope $comboOperand")
                memory.regC = (memory.regA / 2.0.pow(comboOperand)).toInt()
                memory.programCounter += 2
            }
        };

        abstract fun doIt(operand: Int, memory: CPU)
    }

    class CPU(input: List<String>) {
        var regA: Int = input[0].split(":", " ").last().toInt()
        var regB: Int = input[1].split(":", " ").last().toInt()
        var regC: Int = input[2].split(":", " ").last().toInt()
        val program: List<Int> = input.last().split(":", " ").last().split(",").map { it.toInt() }
        var programCounter = 0
        val outputBuffer = emptyList<Int>().toMutableList()

        override fun toString(): String =
            "regA: $regA, regB: $regB, regC: $regC, PG: $programCounter, out: $outputBuffer"

        fun fromComboOperand(operand: Int): Int = when (operand) {
            in 0..3 -> operand
            4 -> regA
            5 -> regB
            6 -> regC
            else -> 0
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
        const val TEST2 = 11

        fun part1(input: List<String>): String {
            val cpu = CPU(input)
            while (cpu.next()) {
                cpu.println()
            }
            return cpu.outputBuffer.joinToString(",")
        }

        fun part2(input: List<String>): Int {
            return TEST2
        }
    }
}

fun main() {

    val day = "Day17"

    val testInput = readInput("${day}_test")
    check(Day17.part1(testInput).also(::println) == Day17.TEST1)
    check(Day17.part2(testInput).also(::println) == Day17.TEST2)

    val input = readInput(day)

    measureTime {
        Day17.part1(input).println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day17.part2(input).println()
    }.also { println("Part2 took $it") }
}