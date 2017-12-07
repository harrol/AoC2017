package day7.part1

import java.io.File

fun main(args: Array<String>) {
    println(solve("input/day7/test-input.txt")) // tknk
    println(solve("input/day7/input.txt")) // puzzle
}

private fun solve(puzzle: String): String {
    val bufferedReader = File(puzzle).bufferedReader()
    val discs = mutableListOf<Disc>()


    bufferedReader.useLines {
        it.forEach { discs.add(parseDisc(it)) }
    }

    return findBase(discs).name
}

private fun findBase(discs: MutableList<Disc>): Disc {
    // Base is disc which holds others but is not held itself
    // find all discs holding others
    // for each, see if it is being held

    val holdingDiscs = discs.filter { it.holding.isNotEmpty() }
            .toList()

    val base = holdingDiscs.find {
        isNotHeld(it, holdingDiscs)
    }

    return base!!
}

private fun isNotHeld(disc: Disc, others: Collection<Disc>): Boolean {
    others.forEach {
        if(it.name != disc.name && it.holding.contains(disc.name)) {
            return false
        }
    }
    return true
}


private fun parseDisc(line: String): Disc {
    // fwft (72) -> ktlj, cntj, xhth
    val discRegex = Regex("""(\w+) \((\d+)\)( -> (.*))?""").matchEntire(line)
    val holding = parseHolding(discRegex!!.groups[4])
    return Disc(discRegex.groups[1]!!.value, discRegex.groups[2]!!.value.toInt(), holding)
}


private fun parseHolding(holding: MatchGroup?): Collection<String> {
    return if(holding == null) {
        emptyList()
    } else {
        holding.value.split(", ")
    }
}

data class Disc(val name: String, val weight: Int, val holding: Collection<String>)
