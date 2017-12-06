package day6

fun main(args: Array<String>) {
    println(solve(arrayOf(0, 2, 7, 0))) // 0 2 7 0 = 5
    // 4	10	4	1	8	4	9	14	5	1	14	15	0	15	3	5
    val puzzle = arrayOf(4, 10, 4, 1, 8, 4, 9, 14, 5, 1, 14, 15, 0, 15, 3, 5)
    println(solve(puzzle))
}

private fun solve(config: Array<Int>): Int {
    var steps = 0
    val distributions = mutableSetOf(config.contentHashCode())

    var current = config

    do {
        steps++
        current = redistribute(current)
    } while (distributions.add(current.contentHashCode()))

    return steps
}

private fun redistribute(config: Array<Int>): Array<Int> {
    val register = findCandidate(config)
    val result = config.copyOf()
    val size = result[register]
    result[register] = 0
    var next = register
    for (i in 1..size) {
        next = if (next < result.size - 1) next + 1 else 0

        result[next] = result[next] + 1
    }

    return result
}

private fun findCandidate(config: Array<Int>): Int {
    var idx = 0
    var max = Int.MIN_VALUE
    for (i in config.indices) {
        if (config[i] > max) {
            max = config[i]
            idx = i
        }
    }
    return idx
}
