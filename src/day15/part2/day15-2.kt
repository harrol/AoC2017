package day15.part2

fun main(args: Array<String>) {
    println(solve(65, 8921)) // 588
    println(solve(783, 325))

}


private fun solve(startA: Int, startB: Int): Int {
    val a = Generator(startA, 16807, 4)
    val b = Generator(startB, 48271, 8)

    var matches = 0

    repeat(5_000_000, {
        if(match(a.next(), b.next())) {
            matches++
        }
    })

    return matches
}

private fun match(a: Long, b: Long): Boolean {
    return a.and(0x0000FFFF) == b.and(0x0000FFFF)
}


class Generator(initial: Int, private val factor: Int, private val multiple: Int) {
    private var lastValue: Long = initial.toLong()

    private fun nextInternal(): Long {
        lastValue = (lastValue * factor).rem(2147483647)
        return lastValue
    }

    fun next(): Long {
        var next = nextInternal()
        while(next.rem(multiple) != 0L) {
            next = nextInternal()
        }
        return next
    }

}