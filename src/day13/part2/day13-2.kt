package day13.part2

import java.io.File

fun main(args: Array<String>) {

    println(solve(parseInput("input/day13/test-input.txt")))
    // Takes way to long, need to optimize
    println(solve(parseInput("input/day13/input.txt")))
}

private fun solve(scanners: List<Scanner>): Int {
    val lastLayer = scanners.maxBy { scanner -> scanner.layer }!!.layer

    var scannerMap = hashMapOf<Int, Scanner>()
    scanners.forEach {
        scannerMap.put(it.layer, it)
    }

    var delay = 0

    do {
        val isCaught = isCaught(lastLayer, copy(scannerMap))

        if(delay % 10000 == 0) println("At $delay")
        delay++

        scannerMap.values.forEach {
            it.next()
        }

    } while (isCaught)

    return delay - 1
}

private fun copy(original: Map<Int, Scanner>): HashMap<Int, Scanner> {
    val newMap = hashMapOf<Int, Scanner>()
    original.forEach { (i, s) ->
        val copy = Scanner(s.layer, s.range, s.position, s.direction)
        newMap.put(i, copy)
    }
    return newMap
}

private fun isCaught(layers: Int, scanners: Map<Int, Scanner>): Boolean {

    for (i in 0..layers) {
        if (scanners[i]?.position == 1) return true
        scanners.values.forEach {
            it.next()
        }
    }

    return false
}


private fun parseInput(input: String): List<Scanner> {
    return File(input).bufferedReader()
            .useLines {
                it.map(::toScanner)
                        .toList()
            }
}


private fun toScanner(line: String): Scanner {
    val split = line.split(":")
    return Scanner(split[0].trim().toInt(), split[1].trim().toInt())
}

data class Scanner(val layer: Int, val range: Int, var position: Int = 1, var direction: Int = 1) {

    fun next() {
        fixDirection()
        position += direction

    }

    fun fixDirection() {
        if (position == range) {
            direction = -1
        }
        if (position == 1) {
            direction = 1
        }
    }

}



