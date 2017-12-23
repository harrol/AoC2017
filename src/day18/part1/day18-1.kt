package day18.part1

import java.io.File

fun main(args: Array<String>) {
    val instructions = parseInput("input/day18/test-input.txt")

    println(solve(instructions))

    println(solve(parseInput("input/day18/input.txt")))
}


private fun parseInput(file: String): List<Instruction> {
    return File(file).bufferedReader()
            .useLines {
                it.map {
                    Instruction.parse(it)
                }.toList()
            }
}

private fun solve(intructions: List<Instruction>): Long {
    var offset = 0
    var frequency = 0L
    val registers = hashMapOf(("a" to 0L), ("b" to 0L), ("f" to 0L), ("i" to 0L), ("p" to 0L))

    while(offset >= 0 && offset < intructions.size) {
        val instruction = intructions[offset]
        print("Executing $instruction at offset $offset. ")
        offset = instruction.exec(offset, registers)
        println("Next offset $offset")
        if(instruction is Sound) {
            frequency = instruction.play(registers)
        }
    }

    return frequency
}

interface Instruction {
    fun exec(offset: Int, registers: HashMap<String, Long>): Int

    fun getValue(value: String, registers: HashMap<String, Long>): Long {
        return registers[value] ?: value.toLong()
    }

    companion object {
        private val opcode = """(set|add|mul|mod|jgz|snd|rcv) ([a-z0-9]) ?(.+)?""".toRegex()
        fun parse(line: String): Instruction {
            if (line.matches(opcode)) {
                val matchResult = opcode.matchEntire(line)!!
                val inst = matchResult.groups[1]!!.value
                val reg = matchResult.groups[2]!!.value
                val value = matchResult.groups[3]?.value

                when (inst) {
                    "set" -> return Set(reg, value!!)
                    "add" -> return Add(reg, value!!)
                    "mul" -> return Mul(reg, value!!)
                    "mod" -> return Mod(reg, value!!)
                    "jgz" -> return Jump(reg, value!!)
                    "snd" -> return Sound(reg)
                    "rcv" -> return Recover(reg)
                }
            }
            throw IllegalArgumentException("Invalid instruction $line")
        }
    }
}

data class Set(val register: String, val value: String) : Instruction {
    override fun exec(offset: Int, registers: HashMap<String, Long>): Int {
        val v = getValue(value, registers)
        registers[register] = v
        return offset + 1
    }
}

data class Add(val register: String, val value: String) : Instruction {
    override fun exec(offset: Int, registers: HashMap<String, Long>): Int {
        val v = getValue(value, registers)
        registers[register] = registers[register]!!.plus(v)
        return offset + 1
    }
}

data class Mul(val register: String, val value: String) : Instruction {
    override fun exec(offset: Int, registers: HashMap<String, Long>): Int {
        val v = getValue(value, registers)
        registers[register] = registers[register]!!.times(v)
        return offset + 1
    }
}

data class Mod(val register: String, val value: String) : Instruction {
    override fun exec(offset: Int, registers: HashMap<String, Long>): Int {
        val v = getValue(value, registers)
        registers[register] = registers[register]!!.rem(v)
        return offset + 1
    }
}


data class Sound(val register: String) : Instruction {
    override fun exec(offset: Int, registers: HashMap<String, Long>): Int {
        return offset + 1
    }

    fun play(registers: HashMap<String, Long>) : Long {
        return getValue(register, registers)
    }
}

data class Recover(val register: String) : Instruction {
    override fun exec(offset: Int, registers: HashMap<String, Long>): Int {
        val condition = getValue(register, registers)
        return if(condition != 0L) 10000 else offset + 1
    }
}

data class Jump(val register: String, val value: String) : Instruction {
    override fun exec(offset: Int, registers: HashMap<String, Long>): Int {
        val newOffset = getValue(value, registers)
        val condition = getValue(register, registers)
        return if(condition > 0L) offset + newOffset.toInt() else offset + 1
    }
}

