

fun main() {

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val part1Test = part1(readInput("DayXX_test"))
    check(part1Test == 11){
        "required 11 but found $part1Test"
    }

    val part2Test = part2(readInput("DayXX_test"))
    check(part2Test == 31){
        "required 31 but found $part2Test"
    }

    val input = readInput("DayXX")
    part1(input).println()
    part2(input).println()
}