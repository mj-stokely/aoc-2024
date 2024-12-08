import kotlin.system.measureTimeMillis

typealias Day7Equation = Long.(Int) -> Long

fun main() {

    data class Data(
        val target: Long,
        val operands: List<Int>,
    )

    fun Long.add(operand: Int): Long = this + operand
    fun Long.multiply(operand: Int): Long = this * operand
    fun Long.concat(operand: Int): Long = "$this$operand".toLong()

    fun parse(input: List<String>): List<Data> =
        input
            .takeWhile { it.isNotBlank() }
            .map { line ->
                val target = line.substringBefore(':').toLong()
                val operands = line.substringAfter(':')
                    .split("""\s+""".toRegex())
                    .filter { it.isNotBlank() }
                    .map { it.toInt() }
                Data(target, operands)
            }

    fun calculate(
        operands: List<Int>,
        operators: List<Day7Equation>,
        position: Int,
    ): Sequence<Long> =
        if (position == operands.lastIndex) {
            sequenceOf(operands[position].toLong())
        } else {
            val nextOperand = operands[position]
            calculate(operands, operators, position + 1)
                .map { runningTotal ->
                    operators.map { operator ->
                        runningTotal.operator(nextOperand)
                    }
                }
                .flatten()
        }

    fun validate(target: Long, operands: List<Int>, operators: List<Day7Equation>): Boolean =
        calculate(operands.reversed(), operators, 0)
            .any { it == target }

    fun part1(input: List<String>): Long {
        val equations = parse(input)
        return equations
            .filter {
                validate(it.target, it.operands, listOf(Long::add, Long::multiply))
            }
            .fold(0L) { acc, current ->
                acc + current.target
            }
    }

    fun part2(input: List<String>): Long {
        val equations = parse(input)
        return equations
            .filter {
                validate(it.target, it.operands, listOf(Long::add, Long::multiply, Long::concat))
            }
            .fold(0L) { acc, current ->
                acc + current.target
            }
    }

    val part1Test = part1(readInput("Day07_test"))
    check(part1Test == 3749L) {
        "part 1 required 3749 but found $part1Test"
    }

    val part2Test = part2(readInput("Day07_test"))
    check(part2Test == 11387L) {
        "part 2 required 11387 but found $part2Test"
    }

    val input = readInput("Day07")
    measureTimeMillis {
        part1(input).println()
    }.also {
        println("part1 ran in : $it ms")
    }
    measureTimeMillis {
        part2(input).println()
    }.also {
        println("part2 ran in : $it ms")
    }
}