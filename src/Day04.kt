typealias Row = String

fun interface Test4 {
    fun verify(game: List<Row>, x: Int, y: Int): Boolean
}

fun main() {

    val lookingFor = listOf('M', 'A', 'S')

    fun countPassingTests(game: List<Row>, x: Int, y: Int, vararg tests: Test4): Int =
        tests.count { it.verify(game, x, y) }

    fun searchGame(game: List<Row>, calculateXY: (offset: Int) -> Pair<Int, Int>): Boolean {
        lookingFor.forEachIndexed { index, c ->
            val (x, y) = calculateXY(index + 1)
            if (y !in 0..game.lastIndex
                || x !in 0..game[y].lastIndex
                || game[y][x] != c
            ) {
                return false
            }
        }
        return true
    }

    fun goUp(game: List<Row>, x: Int, y: Int): Boolean = searchGame(game) { offset ->
        x to y - offset
    }

    fun goDown(game: List<Row>, x: Int, y: Int): Boolean = searchGame(game) { offset ->
        x to y + offset
    }

    fun goLeft(game: List<Row>, x: Int, y: Int): Boolean = searchGame(game) { offset ->
        x - offset to y
    }

    fun goRight(game: List<Row>, x: Int, y: Int): Boolean = searchGame(game) { offset ->
        x + offset to y
    }

    fun goUpLeft(game: List<Row>, x: Int, y: Int): Boolean = searchGame(game) { offset ->
        x - offset to y - offset
    }

    fun goUpRight(game: List<Row>, x: Int, y: Int): Boolean = searchGame(game) { offset ->
        x + offset to y - offset
    }

    fun goDownLeft(game: List<Row>, x: Int, y: Int): Boolean = searchGame(game) { offset ->
        x - offset to y + offset
    }

    fun goDownRight(game: List<Row>, x: Int, y: Int): Boolean = searchGame(game) { offset ->
        x + offset to y + offset
    }

    fun part1(input: List<String>): Int {
        var count = 0
        for (y in 0..input.lastIndex) {
            val row = input[y]
            for (x in 0..row.lastIndex) {
                if (row[x] == 'X') {
                    count += countPassingTests(input, x, y,
                        ::goUp,
                        ::goDown,
                        ::goLeft,
                        ::goRight,
                        ::goUpLeft,
                        ::goUpRight,
                        ::goDownLeft,
                        ::goDownRight,
                    )
                }
            }
        }

        return count
    }

    fun masFromUpLeft(game: List<Row>, x: Int, y: Int): Boolean =
        game[y - 1][x - 1] == 'M' && game[y + 1][x + 1] == 'S'

    fun masFromUpRight(game: List<Row>, x: Int, y: Int): Boolean =
        game[y - 1][x + 1] == 'M' && game[y + 1][x - 1] == 'S'

    fun samFromUpLeft(game: List<Row>, x: Int, y: Int): Boolean =
        game[y - 1][x - 1] == 'S' && game[y + 1][x + 1] == 'M'

    fun samFromUpRight(game: List<Row>, x: Int, y: Int): Boolean =
        game[y - 1][x + 1] == 'S' && game[y + 1][x - 1] == 'M'

    fun masMas(game: List<Row>, x: Int, y: Int): Boolean =
        masFromUpLeft(game, x, y) && masFromUpRight(game, x, y)

    fun masSam(game: List<Row>, x: Int, y: Int): Boolean =
        masFromUpLeft(game, x, y) && samFromUpRight(game, x, y)

    fun samSam(game: List<Row>, x: Int, y: Int): Boolean =
        samFromUpLeft(game, x, y) && samFromUpRight(game, x, y)

    fun samMas(game: List<Row>, x: Int, y: Int): Boolean =
        samFromUpLeft(game, x, y) && masFromUpRight(game, x, y)

    fun part2(input: List<String>): Int {
        var count = 0
        for (y in 0..input.lastIndex) {
            val row = input[y]
            for (x in 0..row.lastIndex) {
                if (input[y][x] == 'A'
                    && x - 1 >= 0
                    && x + 1 < row.length
                    && y - 1 >= 0
                    && y + 1 < row.length
                ) {
                    if (masMas(input, x, y)) count++
                    if (masSam(input, x, y)) count++
                    if (samSam(input, x, y)) count++
                    if (samMas(input, x, y)) count++
                }
            }
        }
        return count
    }

    val part1Test = part1(readInput("Day04_test"))
    check(part1Test == 18) {
        "part 1 required 18 but found $part1Test"
    }

    val part2Test = part2(readInput("Day04_test"))
    check(part2Test == 9) {
        "part 2 required 9 but found $part2Test"
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}