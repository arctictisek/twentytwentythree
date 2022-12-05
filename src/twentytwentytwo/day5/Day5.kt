package twentytwentytwo.day5

import readFile
import safeGetLetter

class Day5 {
    fun run() {
        println(step1())
        println(step2())
    }

    private fun step1(): String {
        val (stacks, moves) = initStacksAndMoves("day5.txt")
        moves.forEach { move ->
                repeat(move[0]) { stacks[move[2] - 1].addFirst(stacks[move[1] - 1].removeFirst()) }
            }
        return stacks.joinToString(separator = "") { it.first() }
    }

    private fun step2(): String {
        val (stacks, moves) = initStacksAndMoves("day5.txt")
        moves.forEach { move ->
                val elements = stacks[move[1] - 1].slice(0 until move[0])
                elements.reversed().forEach { stacks[move[2] - 1].addFirst(it) }
                repeat(move[0]) { stacks[move[1] - 1].removeFirst() }
            }
        return stacks.joinToString(separator = "") { it.first() }
    }

    private fun initStacksAndMoves(fileName: String): Pair<List<ArrayDeque<String>>, List<List<Int>>> {
        val lines = readFile(fileName)
        val stacks = ((1..lines.dropWhile { it.contains('[') }[0].last().digitToInt()).map { _ -> ArrayDeque<String>() }).toList()
        lines.takeWhile { it.contains('[') }.toList().reversed().forEach { line ->
            stacks.forEachIndexed { index, strings ->
                if ("" != (line.safeGetLetter(1 + 4 * index))) {
                    strings.addFirst(line.safeGetLetter(1 + 4 * index))
                }
            }
        }
        val moves = lines.drop(2 + lines.takeWhile { it.contains('[') }.count())
            .map { """move (\d+) from (\d+) to (\d+)""".toRegex().find(it)!!.groupValues }
            .map { it.drop(1) }
            .map { currentLine -> currentLine.map { it.toInt() } }
        return stacks to moves
    }
}
