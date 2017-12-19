package day19.part2

import java.io.File

fun main(args: Array<String>) {

    println(solve(parseFile("input/day19/test-input.txt")))
    println(solve(parseFile("input/day19/input.txt")))

}

private fun parseFile(file: String): Map<Point, Char> {

    val grid = hashMapOf<Point, Char>()

    File(file).bufferedReader()
            .useLines {
                var y = 0
                it.forEach {
                    readLine(it, y, grid)
                    y++
                }
            }


    return grid
}

private fun readLine(line: String, y: Int, grid: HashMap<Point, Char>) {
    for (x in 0 until line.length) {
        var char = line[x]
        if(!char.isWhitespace()) {
            grid.put(Point(x, y), line[x])
        }
    }
}

private fun solve(grid: Map<Point, Char>): Int {
    var steps = 0
    var currentLocation = determineStart(grid)
    var currentDirection = Direction.SOUTH

    while(currentDirection != Direction.STOP) {
        currentDirection = determineNext(currentLocation, currentDirection, grid)
        if(currentDirection == Direction.STOP) break

        currentLocation = currentLocation.neighbour(currentDirection)
        steps++

    }

    return steps
}

fun determineStart(grid: Map<Point, Char>): Point {
    return grid.entries.first { (k, v) -> k.y == 0 && v == '|' }.key
}

fun determineNext(currentLocation: Point, currentDirection: Direction, grid: Map<Point, Char>): Direction {

    val char = grid[currentLocation] ?: return Direction.STOP

    if (char == '|' || char == '-' || char.isLetter()) {
        return currentDirection
    }

    // This must be a crossroad
    when (currentDirection) {
        Direction.SOUTH,
        Direction.NORTH -> {
            return if(grid[currentLocation.neighbour(Direction.EAST)] != null) Direction.EAST else Direction.WEST
        }
        Direction.WEST,
        Direction.EAST -> {
            return if(grid[currentLocation.neighbour(Direction.NORTH)] != null) Direction.NORTH else Direction.SOUTH
        }
    }

    return Direction.STOP
}

data class Point(val x: Int, val y: Int) {
    fun neighbour(direction: Direction): Point {
        return when (direction) {
            Direction.NORTH -> this.copy(y = this.y - 1)
            Direction.SOUTH -> this.copy(y = this.y + 1)
            Direction.EAST -> this.copy(x = this.x + 1)
            Direction.WEST -> this.copy(x = this.x - 1)
            else -> throw IllegalArgumentException("$direction Not a valid direction")
        }
    }
}

enum class Direction(val x: Int, val y: Int) {
    START(0, 0),
    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0),
    STOP(0, 0);
}