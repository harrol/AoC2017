package day15.part1

fun main(args: Array<String>) {
    println(solve(65, 8921)) // 588
    println(solve(783, 325))

}


private fun solve(startA: Int, startB: Int): Int {
    val a = Generator(startA, 16807)
    val b = Generator(startB, 48271)

    var matches = 0

    repeat(40_000_000, {
        if(match(a.next(), b.next())) {
            matches++
        }
    })

    return matches
}

private fun match(a: Long, b: Long): Boolean {
    return a.and(0x0000FFFF) == b.and(0x0000FFFF)
}


class Generator(initial: Int, private val factor: Int) {
    private var lastValue: Long = initial.toLong()

    fun next(): Long {
        lastValue = (lastValue * factor).rem(2147483647)
        return lastValue
    }

}