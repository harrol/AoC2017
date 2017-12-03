package day3.part2

import kotlin.math.ceil
import kotlin.math.sqrt

fun main(args: Array<String>) {
    println(solve(1))
    println(solve(12))
    println(solve(23))
    println(solve(1024))
    println(solve(265149))
}

fun solve(square: Int): Int {
    return initGrid(square)
}


fun initGrid(size: Int): Int {
    val rows = calculateRows(size)
    val row = Array(rows, { _ -> 0 })
    val grid = Array(rows, { _ -> row.copyOf() })

    val center = calculateCenter(rows)
    return fillGrid(center, grid, size)
}

fun fillGrid(start: Point, grid: Array<Array<Int>>, size: Int): Int {
    var solution = 0
    var direction = Direction.START
    var current = calculateLocation(direction, start)

    setValue(start, grid)

    for (i in 2..(grid.size * grid.size)) {
        direction = determineDirection(direction, current, grid)
        current = calculateLocation(direction, current)
        val value = setValue(current, grid)
        if(solution == 0 && value > size) {
            solution = value
        }
    }

    return solution
}


fun determineDirection(previous: Direction, current: Point, grid: Array<Array<Int>>): Direction {
    when (previous) {
        Direction.START -> return Direction.EAST
        Direction.EAST -> {
            // if direction is east and x = row size then we are done
            if (current.x == grid.size) {
                return Direction.STOP
            } else if (getNeighbour(current, Direction.NORTH, grid) == 0) {
                return Direction.NORTH
            } else {
                return Direction.EAST
            }
        }

        Direction.NORTH -> return if (getNeighbour(current, Direction.WEST, grid) == 0) Direction.WEST else Direction.NORTH
        Direction.SOUTH -> return if (getNeighbour(current, Direction.EAST, grid) == 0) Direction.EAST else Direction.SOUTH
        Direction.WEST -> return if (getNeighbour(current, Direction.SOUTH, grid) == 0) Direction.SOUTH else Direction.WEST
        Direction.STOP -> IllegalArgumentException("We where supposed to be done here!")
    }
    return Direction.STOP
}

fun calculateLocation(direction: Direction, current: Point): Point {
    val x = current.x + direction.x
    val y = current.y + direction.y
    return Point(x, y)
}

fun setValue(location: Point, grid: Array<Array<Int>>): Int {
    var value = 0

    value += getNeighbour(location, Direction.NORTH, grid)
    value += getNeighbour(location, Direction.NORTH_EAST, grid)
    value += getNeighbour(location, Direction.EAST, grid)
    value += getNeighbour(location, Direction.SOUTH_EAST, grid)
    value += getNeighbour(location, Direction.SOUTH, grid)
    value += getNeighbour(location, Direction.SOUTH_WEST, grid)
    value += getNeighbour(location, Direction.WEST, grid)
    value += getNeighbour(location, Direction.NORTH_WEST, grid)

    value = if(value > 0) value else 1
    grid[location.x][location.y] = value
    return value
}

fun getNeighbour(start: Point, direction: Direction, grid: Array<Array<Int>>): Int {
    val neighbour = calculateLocation(direction, start)

    if(neighbour.x < 0 || neighbour.x >= grid.size || neighbour.y < 0 || neighbour.y >= grid.size) return 0

    return grid[neighbour.x][neighbour.y]
}

fun calculateRows(square: Int): Int {
    // n*n, n+2*n+2, n+4*n+4, n+6*n+6
    // round up to the nearest odd number from the square root
    // 23 = 4,7 = 5, 10 = 3.16 = 5
    val sqrt = sqrt(square.toDouble())
    val ceil = ceil(sqrt).toInt()
    return if (ceil.rem(2) == 0) ceil + 1 else ceil
}

fun calculateCenter(rows: Int): Point {
    val center = rows.div(2)
    return if (rows < 3) Point(0, 0) else Point(center, center)
}


enum class Direction(val x: Int, val y: Int) {
    START(0, 0),
    NORTH(0, -1),
    NORTH_EAST(1, -1),
    EAST(1, 0),
    SOUTH_EAST(1, 1),
    SOUTH(0, 1),
    SOUTH_WEST(-1, 1),
    WEST(-1, 0),
    NORTH_WEST(-1, -1),
    STOP(0, 0);
}

data class Point(val x: Int, val y: Int)