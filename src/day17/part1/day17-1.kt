package day17.part1

fun main(args: Array<String>) {

    println(solve(3)) // 638
    println(solve(377))
}

fun solve(steps: Int): Int {
    val buffer = arrayListOf(0)

    var index = 0

    for (value in 1..2017) {
        index = calculateInsertionPoint(index, steps, buffer.size)
        buffer.add(index, value)

    }

    return if(index == buffer.size) buffer[0] else buffer[index + 1]
}

private fun calculateInsertionPoint(currentPosition: Int, steps: Int, bufferLength: Int): Int {
    var index = currentPosition
    repeat(steps, {
        index++
        if(index >= bufferLength) index = 0
    })
    return index + 1
}
