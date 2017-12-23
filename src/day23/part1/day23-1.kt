package day23.part1

import java.io.File

fun main(args: Array<String>) {
    val instructions = parseInput("input/day23/input.txt")

    println(solve(instructions))
}


private fun parseInput(file: String): List<Instruction> {
    return File(file).bufferedReader()
            .useLines {
                it.map {
                    Instruction.parse(it)
                }.toList()
            }
}

private fun solve(intructions: List<Instruction>): Int {
    var offset = 0
    var mul = 0
    val registers = hashMapOf(("a" to 0), ("b" to 0), ("c" to 0), ("d" to 0), ("e" to 0), ("f" to 0), ("g" to 0), ("h" to 0))

    while(offset >= 0 && offset < intructions.size) {
        val instruction = intructions[offset]
        print("Executing $instruction at offset $offset. ")
        offset = instruction.exec(offset, registers)
        println("Next offset $offset")
        if(instruction is Mul) {
            mul++
        }
    }

    return mul
}

interface Instruction {
    fun exec(offset: Int, registers: HashMap<String, Int>): Int
    fun getValue(value: String, registers: HashMap<String, Int>): Int {
        return registers[value] ?: value.toInt()
    }

    companion object {
        private val opcode = """(set|sub|mul|jnz) (.+) (.+)""".toRegex()
        fun parse(line: String): Instruction {
            if (line.matches(opcode)) {
                val matchResult = opcode.matchEntire(line)!!
                val inst = matchResult.groups[1]!!.value
                val reg = matchResult.groups[2]!!.value
                val value = matchResult.groups[3]!!.value

                when (inst) {
                    "set" -> return Set(reg, value)
                    "sub" -> return Sub(reg, value)
                    "mul" -> return Mul(reg, value)
                    "jnz" -> return Jump(reg, value)
                }
            }
            throw IllegalArgumentException("Invalid instrucyion $line")
        }
    }
}

data class Set(val register: String, val value: String) : Instruction {
    override fun exec(offset: Int, registers: HashMap<String, Int>): Int {
        val v = getValue(value, registers)
        registers[register] = v
        return offset + 1
    }
}

data class Sub(val register: String, val value: String) : Instruction {
    override fun exec(offset: Int, registers: HashMap<String, Int>): Int {
        val v = getValue(value, registers)
        registers[register] = registers[register]!!.minus(v)
        return offset + 1
    }
}

data class Mul(val register: String, val value: String) : Instruction {
    override fun exec(offset: Int, registers: HashMap<String, Int>): Int {
        val v = getValue(value, registers)
        registers[register] = registers[register]!!.times(v)
        return offset + 1
    }
}

data class Jump(val register: String, val value: String) : Instruction {
    override fun exec(offset: Int, registers: HashMap<String, Int>): Int {
        val newOffset = getValue(value, registers)
        val condition = getValue(register, registers)
        return if(condition != 0) offset + newOffset else offset + 1
    }
}

