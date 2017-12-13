package day13.part1

import java.io.File

fun main(args: Array<String>) {

    println(solve(parseInput("input/day13/test-input.txt")))
    println(solve(parseInput("input/day13/input.txt")))

}

private fun solve(scanners: List<Scanner>): Int {
    val layers = scanners.maxBy { scanner -> scanner.layer }!!.layer
    println("Traversing $layers layers")
    println(scanners)

    var totalDamage = 0

    for(i in 0..layers) {
        totalDamage += damage(i, scanners)

        scanners.forEach{
            it.next()
        }
    }

    return totalDamage
}

private fun parseInput(input: String): List<Scanner> {
    return File(input).bufferedReader()
            .useLines {
                it.map(::toScanner)
                        .toList()
            }
}

private fun damage(layer: Int, scanners: List<Scanner>): Int {
    print("$layer: ")
    val scanner = scanners.find { scanner ->
        scanner.layer == layer
    }
    if(scanner != null) {
        println("$scanner")
        if(scanner.position == 1) {
            println( "Caught!!! ${scanner.range} * $layer")
            return scanner.range * layer
        }
    } else {
        println("Nothing")
    }
    return 0
}


private fun toScanner(line: String): Scanner {
    val split = line.split(":")
    return Scanner(split[0].trim().toInt(), split[1].trim().toInt())
}

data class Scanner(val layer: Int, val range: Int) {
    var position = 1
    private var direction = 1 // 1 = moving down, -1 = moving up

    fun next() {
        position += direction

        if(position == range) {
            direction = -1
        }
        if(position == 1) {
            direction = 1
        }
    }

}



