import kotlin.math.abs

fun main() {

    fun List<String>.parse(): Pair<List<Int>, List<Int>> =
        map {
            val split = it.split("\\s+".toRegex())
            split[0].toInt() to split[1].toInt()
        }.unzip()

    fun part1(input: List<String>): Int {
        val (list1, list2) = input.parse()

        return list1.sorted().zip(list2.sorted()){ a, b ->
            abs(a - b)
        }.sum()
    }

    fun part2(input: List<String>): Long {
        val (list1, list2) = input.parse()

        val count = mutableMapOf<Int, Int>()
        list2.forEach {
            count.putIfAbsent(it, 0)
            count[it] = count[it]!! + 1
        }

        return list1.sumOf {
            it.toLong() * (count[it] ?: 0)
        }
    }

    val part1Test = part1(readInput("Day01_test"))
    check(part1Test == 11){
        "required 11 but found $part1Test"
    }

    val part2Test = part2(readInput("Day01_test"))
    check(part2Test == 31L){
        "required 31 but found $part2Test"
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
