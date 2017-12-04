package day4

import java.io.File

fun main(args: Array<String>) {
    println(solve("input/day4/part1.txt"))
}

private fun solve(file: String): Int {
    val bufferedReader = File(file).bufferedReader()

    val sum = bufferedReader.useLines {
        it.filter(::valid)
                .count()

    }

    return sum
}

private fun valid(passphrase: String): Boolean {

    val uniqueWords = HashSet<String>()

    val words = passphrase.split(" ")

    words.forEach { word -> uniqueWords.add(word) }

    return words.size == uniqueWords.size
}
