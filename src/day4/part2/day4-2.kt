package day4.part2

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

    words.forEach { word -> uniqueWords.add(sortLetters(word)) }

    return words.size == uniqueWords.size
}

private fun sortLetters(passphrase: String): String {

    val sortedSet = passphrase.
            toSortedSet()
    val result = StringBuffer()
    sortedSet.joinTo(result, separator = "")

    return result.toString()
}
