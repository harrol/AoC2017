package day17.part2

fun main(args: Array<String>) {

//    println(solve(3)) // 638
    println(solve(377))
}

fun solve(steps: Int): Int {
    var index = 0
    var second = 0
    var bufferSize = 0

    // Since 0 is always at position 0, we are only interested in the value at position 1.
    // No need to store all values which slows things down to much
    for (value in 1..50_000_000) {
        bufferSize++
        index = calculateInsertionPoint(index, steps, bufferSize)
        if(index == 1) second = value
    }

    return second
}

private fun calculateInsertionPoint(currentPosition: Int, steps: Int, bufferLength: Int): Int {
    var index = currentPosition
    repeat(steps, {
        index++
        if(index >= bufferLength) index = 0
    })
    return index + 1
}
