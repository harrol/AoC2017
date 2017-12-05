package day5.part2

import java.io.File

fun main(args: Array<String>) {
    println(solve("input/day5/part1-test.txt")) // 10
    println(solve("input/day5/part1.txt"))
}

private fun solve(file: String): Long {
    val instructions = File(file).bufferedReader()
            .useLines {
                it.map {
                    it.toInt()
                }.toMutableList()
            }

    var steps: Long = 0
    var pointer = 0

    while(pointer < instructions.size) {
        steps++
        val offset = instructions[pointer]
        val next = pointer + offset
        val modifier = if(offset >= 3) -1 else 1
        instructions[pointer] = instructions[pointer] + modifier
        pointer = next
    }

    return steps
}
