fun main() {

    data class Pose(
        val position: Pair<Int, Int>,
        val orientation: Int,
    )

    class Guard(
        val mapWidth: Int,
        val mapHeight: Int,
        private val startingPoint: Pose,
    ) {
        val visited = mutableSetOf(startingPoint)
        var facingDegrees: Int = 0

        fun reset() {
            visited.clear()
            visited.add(startingPoint)
            facingDegrees = 0
        }

        fun move(obstacles: List<List<Char?>>): Boolean {
            var newPosition = next()
            if (
                isOnMap(newPosition)
                && obstacles[newPosition.second][newPosition.first] != null
            ) {
                rotate()
                newPosition = next()
            }

            return if (isOnMap(newPosition)) {
                val newPose = Pose(newPosition, facingDegrees)
                check(!visited.contains(newPose)) {
                    "looping!"
                }
                visited.add(newPose)
                true
            } else false
        }

//        fun printMap(obstacles: List<List<Char?>>){
//            for (y in 0 until mapHeight) {
//                for (x in 0 until mapWidth) {
//                    if(visited.contains(x to y)){
//                        print("X")
//                    } else {
//                        print(obstacles[y][x] ?: '.')
//                    }
//                }
//                print("\n")
//            }
//        }

        private fun isOnMap(point: Pair<Int, Int>): Boolean =
            point.first in 0 until mapWidth
                && point.second in 0 until mapHeight

        private fun next(): Pair<Int, Int> {
            val current = visited.last().position
            return when (facingDegrees) {
                0 -> current.copy(second = current.second - 1)
                90 -> current.copy(first = current.first + 1)
                180 -> current.copy(second = current.second + 1)
                270 -> current.copy(first = current.first - 1)
                else -> {
                    throw IllegalArgumentException("what are you looking at?! $facingDegrees")
                }
            }
        }

        private fun rotate() {
            facingDegrees += 90
            facingDegrees %= 360
        }
    }

    data class Day6(
        val guard: Guard,
        val obstacles: MutableList<MutableList<Char?>>
    )

    fun parse(input: List<String>): Day6 {
        val width = input.first().length
        val height = input.size
        val obstacles = MutableList<MutableList<Char?>>(height) {
            MutableList(width) { null }
        }

        var startingPoint = 0 to 0

        for (y in 0..input.lastIndex) {
            val row = input[y]
            for (x in 0..row.lastIndex) {
                when (row[x]) {
                    '#' -> obstacles[y][x] = '#'
                    '^' -> startingPoint = x to y
                }
            }
        }

        return Day6(
            Guard(
                width,
                height,
                Pose(startingPoint, 0),
            ),
            obstacles,
        )
    }

    fun part1(input: List<String>): Int {
        val (guard, obstacles) = parse(input)
        while (guard.move(obstacles)) {
//            println(guard.visited.last())
        }
        return guard.visited.distinctBy { it.position }.size
    }

    fun part2(input: List<String>): Int {
        val (guard, obstacles) = parse(input)
        var loops = 0
        for (y in 0..input.lastIndex) {
            for (x in 0..input[y].lastIndex) {
                if (obstacles[y][x] == null) {
                    // try adding new obstacle
                    obstacles[y][x] = '#'
                    try {
                        while (guard.move(obstacles)) {
//                            if(y == 6 && x == 3){
//                                println(guard.visited.last())
//                            }
                        }
                    } catch (e: IllegalStateException) {
                        loops++
                    }
                    obstacles[y][x] = null
                    guard.reset()
                }
                println("tested x: $x, y: $y")
            }
        }

        return loops
    }

    val part1Test = part1(readInput("Day06_test"))
    check(part1Test == 41) {
        "part 1 required 41 but found $part1Test"
    }

    val part2Test = part2(readInput("Day06_test"))
    check(part2Test == 6) {
        "part 2 required 6 but found $part2Test"
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}