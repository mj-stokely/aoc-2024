fun main() {

    val mul = """(?<on>do\(\))|(?<off>don't\(\))|mul\((?<left>[0-9]+),(?<right>[0-9]+)\)""".toRegex()

    fun multiply(match: MatchResult): Int? {
        val left = match.groups["left"]?.value?.toInt()
        val right = match.groups["right"]?.value?.toInt()
        return if (left != null && right != null) {
            left * right
        } else null
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            mul.findAll(line)
                .mapNotNull { match ->
                    multiply(match)
                }
                .sum()
        }
    }

    fun part2(input: List<String>): Int {
        var strategy: ((MatchResult) -> Int?)? = ::multiply
        return input.sumOf { line ->
            mul.findAll(line)
                .mapNotNull { match ->
                    if(match.groups["on"] != null){
                        strategy = ::multiply
                    }
                    if(match.groups["off"] != null){
                        strategy = null
                    }
                    strategy?.invoke(match)
                }
                .sum()
        }

    }

    val part1Test = part1(readInput("Day03_test"))
    check(part1Test == 161) {
        "required 161 but found $part1Test"
    }

    val part2Test = part2(readInput("Day03_test"))
    check(part2Test == 48) {
        "required 31 but found $part2Test"
    }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}