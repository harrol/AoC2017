package day13.part2

import java.io.File

fun main(args: Array<String>) {

    println(solve(parseInput("input/day13/test-input.txt")))

    // Takes way to long, need to optimize
    println(solve(parseInput("input/day13/input.txt")))

}

private fun solve(scanners: List<Scanner>): Int {
    val layers = scanners.maxBy { scanner -> scanner.layer }!!.layer


    var scannerMap = hashMapOf<Int, Scanner>()
    scanners.forEach {
        scannerMap.put(it.layer, it)
    }

    var delay = 0

    while (isCaught(layers, scannerMap)) {

        scannerMap.values.forEach {
            it.reset()
        }

        repeat(delay, {
            scannerMap.values.forEach {
                it.next()
            }
        })

        delay++

        if (delay % 1000 == 0) println("At $delay")
    }

    println(scanners)

    return delay - 1
}

private fun copy(original: Map<Int, Scanner>):  Map<Int, Scanner> {
    val newMap = hashMapOf<Int, Scanner>()
    original.forEach{ (i, s) -> newMap.put(i, s.copy())}
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

data class Scanner(val layer: Int, val range: Int, var position: Int = 1) {
    private var direction = 1 // 1 = moving down, -1 = moving up

    fun next() {
        position += direction

        if (position == range) {
            direction = -1
        }
        if (position == 1) {
            direction = 1
        }
    }

    fun reset() {
        position = 1
        direction = 1
    }

}



