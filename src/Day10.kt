import kotlin.system.measureNanoTime

fun main() {

    data class Point(val x: Int, val y: Int)

    data class Grid(
        val elevations: List<List<Int>>,
        val trailHeads: Set<Point>,
    ) {
        val widthRange: IntRange = elevations.first().indices
        val heightRange: IntRange = elevations.indices

        val Point.elevation: Int
            get() = elevations[y][x]

        fun scoreAllTrailsPartOne(): Int = trailHeads.sumOf {
//            println("start at: $it")
            scoreUnique(it)
        }

        fun scoreAllTrailsPartTwo(): Int = trailHeads.sumOf {
//            println("start at: $it")
            scoreAll(it)
        }

        fun scoreUnique(head: Point): Int = scoreHelper(head).toSet().size

        fun scoreAll(head: Point): Int = scoreHelper(head).size

        fun scoreHelper(point: Point): List<Point> {
//            println("search from : $point")
            if (atPeak(point)) {
//                println("peak at: $point")
                return listOf(point)
            }

            fun Point?.searchOrEmpty() = this?.let {
                scoreHelper(it)
            } ?: emptyList()

            return mutableListOf<Point>().apply {
                addAll(up(point).searchOrEmpty())
                addAll(down(point).searchOrEmpty())
                addAll(left(point).searchOrEmpty())
                addAll(right(point).searchOrEmpty())
            }
        }

        fun atPeak(p: Point): Boolean = p.elevation == 9
        fun up(p: Point): Point? = p.copy(y = p.y - 1).takeIfOnGrid().takeIfNextElevation(p.elevation)
        fun down(p: Point): Point? = p.copy(y = p.y + 1).takeIfOnGrid().takeIfNextElevation(p.elevation)
        fun left(p: Point): Point? = p.copy(x = p.x - 1).takeIfOnGrid().takeIfNextElevation(p.elevation)
        fun right(p: Point): Point? = p.copy(x = p.x + 1).takeIfOnGrid().takeIfNextElevation(p.elevation)

        private fun Point.takeIfOnGrid(): Point? =
            takeIf { it.x in widthRange && it.y in heightRange }

        private fun Point?.takeIfNextElevation(currentElevation: Int): Point? =
            this?.takeIf { elevation == currentElevation + 1 }
    }

    fun parse(input: List<String>): Grid {
        val heads = input
            .mapIndexed { y, line ->
                line.mapIndexedNotNull { x, c ->
                    if (c == '0') {
                        Point(x, y)
                    } else null
                }
            }
            .flatten()
            .toSet()

        val elevations = input.map { line -> line.map { it.digitToInt() } }

        return Grid(
            elevations,
            heads,
        )
    }

    fun part1(input: List<String>): Int = parse(input).scoreAllTrailsPartOne()

    fun part2(input: List<String>): Int = parse(input).scoreAllTrailsPartTwo()

    val part1Test = part1(readInput("Day10_test"))
    check(part1Test == 36) {
        "part 1 required 36 but found $part1Test"
    }

    val part2Test = part2(readInput("Day10_test"))
    check(part2Test == 81) {
        "part 2 required 81 but found $part2Test"
    }

    val input = readInput("Day10")
    part1(input).println()
    measureNanoTime {
        part2(input).println()
    }.also {
        println("finished in: $it ns")
    }

}