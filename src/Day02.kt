import kotlin.math.abs


fun main() {

    data class Report(
        val levels: List<Int>,
    ) {
        fun isMostlySafe(): Boolean {
            val hasSafeDelta = { previous: Int, current: Int ->
                val delta = abs(current - previous)
                delta in 1..3
            }
            val isAsc = { previous: Int, current: Int ->
                previous <= current
            }
            val isDesc = { previous: Int, current: Int ->
                previous >= current
            }

            return levels.indices.any {
                runAction(it, hasSafeDelta)
                    && (runAction(it, isAsc) || runAction(it, isDesc))
            }.also {
                if(!it){
                    println("failed report: $levels")
                }
            }
        }

        private fun runAction(skip: Int, action: (previous: Int, current: Int) -> Boolean): Boolean {
            val previousIdx = if(skip == 0) 1 else 0
            var previous = levels[previousIdx]
            for (i in previousIdx + 1..levels.lastIndex) {
                if (i == skip) {
                    continue
                }

                val current = levels[i]
                val valid = action(previous, current)
                if (!valid) {
                    return false
                }
                previous = current
            }
            return true
        }

        fun isSafe(): Boolean {
            var previous = levels[0]
            for (i in 1..levels.lastIndex) {
                val current = levels[i]
                val delta = abs(current - previous)
                if (delta !in 1..3) {
                    return false
                }
                previous = current
            }

            val asc = levels.sorted()
            val desc = asc.reversed()

            return levels.isSameAs(asc)
                || levels.isSameAs(desc)
        }

        private fun List<Int>.isSameAs(other: List<Int>): Boolean {
            if (size != other.size) return false
            for (i in 0..lastIndex) {
                if (this[i] != other[i]) {
                    return false
                }
            }
            return true
        }
    }

    fun parse(input: List<String>): List<Report> =
        input.map { line ->
            val levels = line.split("""\s+""".toRegex()).map { it.toInt() }
            Report(levels)
        }

    fun part1(input: List<String>): Int {
        return parse(input)
            .count { it.isSafe() }
    }

    fun part2(input: List<String>): Int {
        return parse(input)
            .count { it.isMostlySafe() }
    }

    val part1Test = part1(readInput("Day02_test"))
    check(part1Test == 2) {
        "required 2 but found $part1Test"
    }

    val part2Test = part2(readInput("Day02_test"))
    check(part2Test == 8) {
        "required 8 but found $part2Test"
    }

    val input = readInput("Day02")
//    part1(input).println()
    part2(input).println()
}