package day2.part1

import java.io.File

fun main(args: Array<String>) {
    println(solve("input/day2/test.txt"))
    println(solve("input/day2/input.txt"))
}

fun solve(file: String): Int {
    val bufferedReader = File(file).bufferedReader()

    val sum = bufferedReader.useLines {
        it.map(::hash)
                .sum()
    }

    return sum
}

fun hash(line: String): Int {
    var min = Int.MAX_VALUE
    var max = Int.MIN_VALUE

    line.splitToSequence(" ")
            .forEach {
                val value = it.toString().toInt()
                if (min > value) min = value
                if (max < value) max = value
            }

    return max - min
}