package day3.part1

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.sqrt

fun main(args: Array<String>) {
    println(solve1(1)) // 0
    println(solve1(12)) // 3
    println(solve1(23)) // 2
    println(solve1(1024)) // 31
    println(solve1(265149)) // 438
}

fun solve1(square: Int): Int {
    val grid = initGrid(square)

    val location = findLocation(square, grid.grid)

    return location.distance(grid.center)
}


fun printgrid(grid: Array<Array<Int>>) {
    println("Grid  ${grid.size}x${grid.size}")
    for(y in 0 until grid.size) {
        for(x in 0 until grid[y].size) {
            print("\t${grid[x][y]}")
        }
        println()
    }
    println()
}

fun initGrid(size: Int): Grid {
    val rows = calculateRows(size)
    val row = Array(rows, { _ -> -1 })
    val grid = Array(rows, { _ -> row.copyOf() })

    val center = calculateCenter(rows)
    fillGrid(center, grid)
    return Grid(center, grid)
}

fun fillGrid(start: Point, grid: Array<Array<Int>>) {
    var direction = Direction.START
    var current = calculate(direction, start)

    setValue(1, start, grid)

    for (i in 2..(grid.size * grid.size)) {
        direction = determineDirection(direction, current, grid)
        current = calculate(direction, current)
        setValue(i, current, grid)
    }

    printgrid(grid)
}

fun findLocation(value: Int, grid: Array<Array<Int>>): Point {
    for(y in 0 until grid.size) {
        (0 until grid[y].size)
                .filter { grid[it][y] == value }
                .forEach { return Point(it, y) }
    }
    return Point(0, 0)
}

fun determineDirection(previous: Direction, current: Point, grid: Array<Array<Int>>): Direction {
    when (previous) {
        Direction.START -> return Direction.EAST
        Direction.EAST -> {
            // if direction is east and x = row size then we are done
            if (current.x == grid.size) {
                return Direction.STOP
            } else if (getNeighbour(current, Direction.NORTH, grid) == -1) {
                return Direction.NORTH
            } else {
                return Direction.EAST
            }
        }

        Direction.NORTH -> return if (getNeighbour(current, Direction.WEST, grid) == -1) Direction.WEST else Direction.NORTH
        Direction.SOUTH -> return if (getNeighbour(current, Direction.EAST, grid) == -1) Direction.EAST else Direction.SOUTH
        Direction.WEST -> return if (getNeighbour(current, Direction.SOUTH, grid) == -1) Direction.SOUTH else Direction.WEST
        Direction.STOP -> IllegalArgumentException("We where supposed to be done here!")
    }
    return Direction.STOP
}

fun calculate(direction: Direction, current: Point): Point {
    val x = current.x + direction.x
    val y = current.y + direction.y
    return Point(x, y)
}

fun setValue(value: Int, location: Point, grid: Array<Array<Int>>) {
    grid[location.x][location.y] = value
}

fun getNeighbour(start: Point, direction: Direction, grid: Array<Array<Int>>): Int {
    val neighbour = calculate(direction, start)

    val i = grid[neighbour.x][neighbour.y]
    return i
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
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0),
    STOP(0, 0);
}

data class Point(val x: Int, val y: Int) {
    fun distance(other: Point): Int {
        return abs(x - other.x) + abs(y - other.y)
    }
}

data class Grid(val center: Point, val grid: Array<Array<Int>>)