package day12.part1

import java.io.File

fun main(args: Array<String>) {

    val programs = readInput("input/day12/test-input.txt")

    println(programs)

    val first = programs[0]!!
    val count = programs.values
            .drop(1)
            .sumBy { if(hasRouteToZero(first, it, programs)) 1 else 0 }

    println(count)
}

private fun hasRouteToZero(from: Program, current: Program, programs: Map<Int, Program>): Boolean {
    // Direct connection exists, we are done here
    if (current.id == 0 || current.connections.contains(0)) {
        println("yes")
        return true
    }

    // No more connections to visit, back to where we came from
    if (current.toVisit.isEmpty()) {
        return false
    }

    // Visit next connection of current if available
    val nextProgramId = current.toVisit.elementAt(0)
    current.toVisit.remove(nextProgramId)

    // Self reference?
    if(nextProgramId == current.id) {
        // has other reference?
        if(current.toVisit.isEmpty()) {
            return false
        } else {
            val nextNext = current.toVisit.elementAt(0)
            current.toVisit.remove(nextNext)
            val nextNextProgram = programs[nextNext]!!
            return hasRouteToZero(from, nextNextProgram, programs)
        }
    }

    val nextProgram = programs[nextProgramId]!!
    return hasRouteToZero(current, nextProgram, programs)

}

private fun readInput(file: String): Map<Int, Program> {
    return File(file).bufferedReader()
            .useLines {
                it.map(::parseProgram)
                        .toList()
                        .map{it.id to it}
                        .toMap()
            }
}


private fun parseProgram(line: String): Program {
    val splitted = line.split(" <-> ")
    val id = splitted[0].trim().toInt()
    val connections = splitted[1].splitToSequence(", ")
            .map { it.toInt() }
            .toMutableSet()
    return Program(id, HashSet(connections), connections)
}


data class Program(val id: Int, val connections: Set<Int>, var toVisit: MutableSet<Int>)