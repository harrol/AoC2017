package day11.part1

import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    println(solve(parse("ne,ne,ne"))) // 3
    println(solve(parse("ne,ne,sw,sw"))) // 0
    println(solve(parse("ne,ne,s,s"))) // 2
    println(solve(parse("se,sw,se,sw,sw"))) // 3


    val bufferedReader = File("input/day11/input.txt").bufferedReader()
    val line = bufferedReader.readLine()
    println(solve(parse(line)))

}

private fun parse(input: String): List<Direction> {
    return input.splitToSequence(",")
            .map { Direction.valueOf(it) }
            .toList()
}

private fun solve(steps: List<Direction>): Int {
    val start = Point(0, 0)
    var location = start

    steps.forEach({
        location = move(location, it)
    })


    return start.distance(location)
}

private fun move(from: Point, to: Direction): Point {
    return Point(from.x + to.x, from.y + to.y)
}

data class Point(val x: Int, val y: Int) {
    fun distance(other: Point): Int {
        return (abs(x - other.x) + abs(y - other.y)) / 2
    }
}

enum class Direction(val x: Int, val y: Int) {
    n(0, 2),
    ne(1, 1),
    se(1, -1),
    s(0, -2),
    sw(-1, -1),
    nw(-1, 1)
}
