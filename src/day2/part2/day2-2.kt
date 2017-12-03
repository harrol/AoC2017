package day2.part2

import java.io.File

fun main(args: Array<String>) {
    println(solve("input/day2/testB.txt"))
    println(solve("input/day2/input.txt"))
}

fun solve(file: String): Int {
    val bufferedReader = File(file).bufferedReader()

    val sum = bufferedReader.useLines {
        it.map(::lineHash)
                .sum()
    }

    return sum
}

private fun lineHash(line: String): Int {
    val numbers = line.split(" ")
    for (i in numbers.indices) {
        for (j in numbers.indices) {
            val evenly = divideEvenly(numbers[i], numbers[j])
            if (i != j && evenly > 0) {
                return evenly
            }
        }
    }
    return 0
}

private fun divideEvenly(s1: String, s2: String): Int {
    val x = s1.toInt()
    val y = s2.toInt()
    return if (x.rem(y) == 0) {
        x / y
    } else 0
}