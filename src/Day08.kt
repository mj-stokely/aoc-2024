import kotlin.math.abs

fun main() {

    data class Point(val x: Int, val y: Int){
        operator fun plus(other: Point) = Point(x + other.x, y + other.y)
        operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    }

    data class Grid(
        val width: Int,
        val height: Int,
        val stations: Map<Char, List<Point>>,
    ) {
        fun findResonateNodes(a: Point, b: Point): Set<Point> {
            check(a != b) {
                "points equal: $a"
            }

            val result = mutableSetOf<Point>()
            val distance = b - a
            var node = a
            do{
                node += distance
                val onGrid = isOnGrid(node)
                if(onGrid){
                    result += node
                }
            } while(onGrid)
            return result
        }

        fun findAntinodes(a: Point, b: Point): Set<Point> {
            check(a != b) {
                "points equal: $a"
            }
            val run = abs(a.x - b.x)
            val rise = if (a.x > b.x) a.y - b.y else b.y - a.y

            val nodes =
                if (b.x > a.x) {
                    listOf(
                        Point(b.x + run, b.y + rise),
                        Point(a.x - run, a.y - rise)
                    )
                } else {
                    listOf(
                        Point(b.x - run, b.y - rise),
                        Point(a.x + run, a.y + rise)
                    )
                }

            return nodes
                .filter { isOnGrid(it) }
                .toSet()
        }

        fun isOnGrid(point: Point): Boolean =
            point.x in 0..<width
                && point.y in 0..<height
    }

    val stations = """[A-Za-z\d]""".toRegex()

    fun parse(input: List<String>): Grid {
        val result = mutableMapOf<Char, MutableList<Point>>()
        input
            .reversed() // i just want y to go 'up' when working with line slopes later
            .forEachIndexed { y, line ->
                line.forEachIndexed { x, c ->
                    if (stations.matchesAt(line, x)) {
                        result.putIfAbsent(c, mutableListOf())
                        result[c]!!.add(Point(x, y))
                    }
                }
            }
        return Grid(
            input.first().length,
            input.size,
            result,
        )
    }


    fun part1(input: List<String>): Int {
        val data = parse(input)
        val nodes = data.stations
            .map {
                val antinodes = mutableSetOf<Point>()
                val points = it.value
                for (i in 0..points.lastIndex) {
                    for (j in i + 1..points.lastIndex) {
                        val a = points[i]
                        val b = points[j]
                        val newNodes = data.findAntinodes(a, b)
//                        println("${it.key} -> $a to $b $\n${newNodes.joinToString("\n")}\n")
                        antinodes.addAll(newNodes)
                    }
                }

                antinodes
            }
            .fold(mutableSetOf<Point>()) { acc, next ->
                acc.union(next).toMutableSet()
            }
        return nodes.size
    }

    fun part2(input: List<String>): Int {
        val data = parse(input)
        val nodes = data.stations
            .map {
                val antinodes = mutableSetOf<Point>()
                val points = it.value
                for (i in 0..points.lastIndex) {
                    for (j in 0..points.lastIndex) {
                        val a = points[i]
                        val b = points[j]
                        if(a == b) continue

                        val newNodes = data.findResonateNodes(a, b)
//                        println("${it.key} -> $a to $b $\n${newNodes.joinToString("\n")}\n")
                        antinodes.addAll(newNodes)
                    }
                }
                antinodes
            }
            .fold(mutableSetOf<Point>()) { acc, next ->
                acc.union(next).toMutableSet()
            }
        return nodes.size
    }

    val part1Test = part1(readInput("Day08_test"))
    check(part1Test == 14) {
        "part 1 required 14 but found $part1Test"
    }

    val part2Test = part2(readInput("Day08_test"))
    check(part2Test == 34) {
        "part 2 required 34 but found $part2Test"
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}