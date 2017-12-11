package day8.part1

import java.io.File


fun main(args: Array<String>) {

    println(solve("input/day8/test-input.txt"))
    println(solve("input/day8/input.txt"))

}

fun solve(file: String): Int {
    var instructions = mutableListOf<Instruction>()
    var registers = mutableMapOf<String, Register>()

    File(file).bufferedReader()
            .useLines {
                it.forEach {
                    val instruction = parseInstruction(it)
                    instructions.add(instruction)
                    registers.put(instruction.register, Register(instruction.register, 0))
                }
            }


    instructions.forEach({
        if(it.condition.evaluate(registers)) {
            // regValue +- value
            val register = registers.get(it.register)!!
            val newValue = it.operation.calc(register.value, it.value)
            register.value = newValue
        }
    })

    return registers.maxBy { it.value.value }!!
            .value.value
}

fun parseInstruction(line: String): Instruction {
    // register operation value condition
    // b inc 5 if a > 1

    val instRegex = Regex("""(\w+) (\w+) (-?\d+) (.+)""").matchEntire(line)!!

    val register = instRegex.groups[1]!!.value
    val operation = Operation.parse(instRegex.groups[2]!!.value)
    val value = instRegex.groups[3]!!.value.toInt()
    val condition = parseCondition(instRegex.groups[4]!!.value)

    return Instruction(register, operation, value, condition)
}

fun parseCondition(line: String): Condition {
    // if a > 5
    val condRegex = Regex("""if (\w+) ([<>!=]+) (-?\d+)""").matchEntire(line)!!
    return Condition(condRegex.groups[1]!!.value, ConditionExp.parse(condRegex.groups[2]!!.value), condRegex.groups[3]!!.value.toInt())
}

class Instruction(val register: String, val operation: Operation, val value: Int, val condition: Condition) {

    override fun toString(): String {
        return "Instruction(register=$register, operation=$operation, value=$value, condition=$condition)"
    }
}

data class Register(val name: String, var value: Int)

enum class Operation(val encoding: String) {
    INC("inc") {
        override fun calc(lhs: Int, rhs: Int): Int {
            return lhs + rhs
        }
    },
    DEC("dec") {
        override fun calc(lhs: Int, rhs: Int): Int {
            return lhs - rhs
        }
    };

    companion object {
        fun parse(encoded: String): Operation {
            return Operation.values().find {
                encoded == it.encoding
            }!!
        }
    }

    abstract fun calc(lhs: Int, rhs: Int): Int
}

class Condition(val registerName: String, val conditionExp: ConditionExp, val value: Int) {

    fun evaluate(registers: Map<String, Register>): Boolean {
        val currentRegisterValue = registers.get(this.registerName)!!.value
        return conditionExp.eval(currentRegisterValue, value)
    }

    override fun toString(): String {
        return "Condition($registerName ${conditionExp.encoded} $value)"
    }
}

enum class ConditionExp(val encoded: String) {
    GT(">") {
        override fun eval(lhs: Int, rhs: Int): Boolean {
            return lhs > rhs
        }
    },
    LT("<") {
        override fun eval(lhs: Int, rhs: Int): Boolean {
            return lhs < rhs
        }
    },
    GE(">=") {
        override fun eval(lhs: Int, rhs: Int): Boolean {
            return lhs >= rhs
        }
    },
    LE("<=") {
        override fun eval(lhs: Int, rhs: Int): Boolean {
            return lhs <= rhs
        }
    },
    EQ("==") {
        override fun eval(lhs: Int, rhs: Int): Boolean {
            return lhs == rhs
        }
    },
    NE("!=") {
        override fun eval(lhs: Int, rhs: Int): Boolean {
            return lhs != rhs
        }
    };

    companion object {
        fun parse(encoded: String): ConditionExp {
            return ConditionExp.values().find {
                it.encoded == encoded
            }!!
        }
    }

    abstract fun eval(lhs: Int, rhs: Int): Boolean
}