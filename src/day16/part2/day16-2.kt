package day16.part2

import java.io.File

fun main(args: Array<String>) {

    println(solve("abcdefghijklmnop", parseInput("input/day16/input.txt")))

}


private fun solve(startPosition: String, moves: List<Move>): String {

    var dance = startPosition

    // Pattern repeats after 60 iterations
    // So only need 1_000_000_000.rem(60) = 40 iterations
    val iterations = 1_000_000_000.rem(60)
    for(i in 1..iterations) {
        moves.forEach {
            dance = it.execute(dance)
            if (dance == startPosition) {
                println("Repeat after $i iterations")
            }
        }
    }

    return dance
}


private fun parseInput(file: String): List<Move> {
    return File(file).bufferedReader()
            .readLine()
            .splitToSequence(",")
            .map { Move.parseMove(it) }
            .toList()
}

interface Move {
    fun execute(line: String): String

    companion object {
        val spin = """s(\d{1,2})""".toRegex()
        val exchange = """x(\d{1,2})/(\d{1,2})""".toRegex()
        val partner = """p(\w)/(\w)""".toRegex()

        fun parseMove(move: String): Move {
            if (spin.matches(move)) return Spin(spin.matchEntire(move)!!.groups[1]!!.value.toInt())
            if (exchange.matches(move)) return Exchange(exchange.matchEntire(move)!!.groups[1]!!.value.toInt(), exchange.matchEntire(move)!!.groups[2]!!.value.toInt())
            if (partner.matches(move)) return Partner(partner.matchEntire(move)!!.groups[1]!!.value[0], partner.matchEntire(move)!!.groups[2]!!.value[0])
            throw IllegalArgumentException("Unknown move $move")
        }
    }
}

class Spin(val programs: Int) : Move {
    // s1, makes a programs move from end to front
    override fun execute(line: String): String {
        val toFront = line.substring(line.length - programs)
        val toBack = line.substring(0, line.length - programs)
        return toFront + toBack
    }
}

class Exchange(val position1: Int, val position2: Int) : Move {
    // x3/4, position 3 and 4 swap
    override fun execute(line: String) : String {
        val chars = line.toCharArray()
        val c1 = chars[position1]
        val c2 = chars[position2]
        chars[position1] = c2
        chars[position2] = c1
        return chars.joinToString("")
    }
}

class Partner(val program1: Char, val program2: Char) : Move {
    // pe/d, swap program e and d
    override fun execute(line: String) :String {
        val chars = line.toCharArray()
        val p1 = chars.indexOf(program1)
        val p2 = chars.indexOf(program2)
        chars[p1] = program2
        chars[p2] = program1
        return chars.joinToString("")
    }
}