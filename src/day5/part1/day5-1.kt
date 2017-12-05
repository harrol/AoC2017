package day5.part1

import java.io.File

fun main(args: Array<String>) {
    println(solve("input/day5/part1-test.txt")) // 5
    println(solve("input/day5/part1.txt"))
}

private fun solve(file: String): Int {
    val instructions = File(file).bufferedReader()
            .useLines {
                it.map {
                    it.toInt()
                }.toMutableList()
            }

    var steps = 0
    var pointer = 0

    while(pointer < instructions.size) {
        steps++
        val next = pointer + instructions[pointer]
        instructions[pointer] = instructions[pointer] + 1
        pointer = next
    }

    return steps
}
