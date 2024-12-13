import Parser9.Strategy
import java.util.Stack

class Parser9(val input: String) {
    private var fileId = 0
    private val free = -1

    private val fileIds = Stack<FileId>()
    private val freeSpace = mutableListOf<IntRange>()
    private val IntRange.size: Int
        get() = last - first + 1

    fun interface Strategy {
        fun appendInputAt(dest: MutableList<Int>, position: Int)
    }

    private val fileIdStrategy: Strategy = Strategy { dest, position ->
        val blockCount = input[position].digitToInt()
        val first = dest.size
        repeat(blockCount) {
            dest.add(fileId)
        }
        fileIds.push(
            FileId(
                fileId,
                first..<first + blockCount
            ),
        )
        fileId++
        currentStrategy = emptySpaceStrategy
    }

    private val emptySpaceStrategy: Strategy = Strategy { dest, position ->
        val blockCount = input[position].digitToInt()
        val first = dest.size
        repeat(blockCount) {
            dest.add(free)
        }
        freeSpace.add(first ..<first + blockCount)
        currentStrategy = fileIdStrategy
    }

    private var currentStrategy: Strategy = fileIdStrategy

    fun parse(): MutableList<Int> {
        val result = mutableListOf<Int>()
        (0..input.lastIndex).forEach {
            currentStrategy.appendInputAt(result, it)
        }
        return result
    }

    fun defrag(memory: MutableList<Int>) {
        var left = 0
        var right = memory.lastIndex
        while (left < right) {
            if (memory[left] != free) {
                left++
            } else if (memory[right] == free) {
                right--
            } else {
                memory[left] = memory[right]
                memory[right] = free
                left++
                right--
            }
        }
    }

    fun defragFullIds(memory: MutableList<Int>) {
        while (fileIds.isNotEmpty()) {
            val current = fileIds.pop()

            freeSpace
                .indexOfFirst {
                    it.size >= current.memoryPositions.size
                        && it.last < current.memoryPositions.first
                }
                .takeIf { it >= 0 }
                ?.let {
                    val freeSpaceRange = freeSpace[it]
                    val idBegin = current.memoryPositions.first

                    repeat(current.memoryPositions.size) { offset ->
                        memory[freeSpaceRange.first + offset] = current.id
                        memory[idBegin + offset] = free
                    }

                    val newFreeSpaceStart = freeSpaceRange.first + current.memoryPositions.size
                    if(newFreeSpaceStart > freeSpaceRange.last){
                        freeSpace.removeAt(it)
                    } else {
                        freeSpace[it] = newFreeSpaceStart..freeSpaceRange.last
                    }
                }
        }
    }

    fun checksum(memory: List<Int>): Long {
        var result = 0L
        memory.indices.forEach { i ->
            if (memory[i] != free) {
                check(Long.MAX_VALUE - result >= memory[i] * i)
                result += memory[i] * i
            }
        }
        return result
    }

    data class FileId(
        val id: Int,
        val memoryPositions: IntRange,
    )
}

fun main() {

    fun part1(input: List<String>): Long {
        with(Parser9(input.first())) {
            val memory = parse()
            defrag(memory)
            return checksum(memory)
        }
    }

    fun part2(input: List<String>): Long {
        with(Parser9(input.first())) {
            val memory = parse()
//            memory.joinToString("") { if (it == -1) "." else it.toString() }.println()
            defragFullIds(memory)
            return checksum(memory)
        }
    }

    val part1Test = part1(readInput("Day09_test"))
    check(part1Test == 1928L) {
        "part 1 required 1928 but found $part1Test"
    }

    val part2Test = part2(readInput("Day09_test"))
    check(part2Test == 2858L) {
        "part 2 required 2858 but found $part2Test"
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}