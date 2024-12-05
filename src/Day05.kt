data class Data5(
    val rules: Map<Int, List<Int>>,
    val updates: List<List<Int>>,
)

fun main() {

    fun parse(input: List<String>): Data5 {
        val rules = mutableMapOf<Int, MutableList<Int>>()
        val updates = mutableListOf<List<Int>>()
        var i = 0
        do {
            val line = input[i++]
            val (key, value) = line.split("|").let { it[0].toInt() to it[1].toInt() }
            rules.putIfAbsent(key, mutableListOf())
            rules[key]!!.add(value)
        } while (input[i].isNotBlank())

        (i + 1..input.lastIndex)
            .map { idx ->
                input[idx]
                    .split(",")
                    .map { it.toInt() }
            }
            .forEach {
                updates.add(it)
            }

        return Data5(rules, updates)
    }

    fun isUpdateValid(
        update: List<Int>,
        rules: Map<Int, List<Int>>,
    ): Boolean {
        for (i in 0..update.lastIndex) {
            val value = update[i]
            for (j in i + 1..update.lastIndex) {
                val key = update[j]
                if (rules[key]?.contains(value) == true) {
                    return false
                }
            }
        }
        return true
    }

    fun List<Int>.bubbleSort(rules: Map<Int, List<Int>>): List<Int> {
        val result = toMutableList()
        do{
            var finished = true
            for(i in 0 until result.lastIndex){
                val current = result[i]
                val next = result[i + 1]
                if(rules[next]?.contains(current) == true){
                    result[i] = next
                    result[i + 1] = current
                    finished = false
                }
            }
        } while(!finished)
        return result
    }


    fun part1(input: List<String>): Int {
        val data = parse(input)
        return data.updates
            .filter { update ->
                isUpdateValid(update, data.rules)
            }
            .sumOf {
                it[it.size / 2]
            }
    }

    fun part2(input: List<String>): Int {
        val data = parse(input)
        return data.updates
            .filterNot { update ->
                isUpdateValid(update, data.rules)
            }
            .map {
                it.bubbleSort(data.rules)
            }
            .sumOf {
                it[it.size / 2]
            }
    }

    val part1Test = part1(readInput("Day05_test"))
    check(part1Test == 143) {
        "part 1 required 143 but found $part1Test"
    }

    val part2Test = part2(readInput("Day05_test"))
    check(part2Test == 123){
        "part 2 required 123 but found $part2Test"
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}