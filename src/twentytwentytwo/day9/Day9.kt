package twentytwentytwo.day9

import readFile
import kotlin.math.abs
import kotlin.math.max

class Day9 {
    fun run() {
        println(step1())
        println(step2())
        println(headAndTailTouch(1 to 3, 1 to 1))
    }

    private fun step1(): Int {
        var curPosHead = 0 to 0
        var curPosTail = 0 to 0
        val visited = mutableSetOf<Pair<Int, Int>>()
        val moves = readFile("day9Simple.txt", 2022)
            .map { it.split(' ').zipWithNext().single() }
            .map { it.first to (0 until it.second.toInt()) }
            .flatMap { stringAndRange -> stringAndRange.second.map { stringAndRange.first } }
        var tailMoves = listOf<String>()
        moves.forEach {
            val result = move(curPosHead, curPosTail, it, visited, tailMoves)
            curPosHead = result.first.first
            curPosTail = result.first.second
            tailMoves = result.third
        }
        return visited.size
    }

    private fun move(
        head: Pair<Int, Int>,
        tail: Pair<Int, Int>,
        move: String,
        visitedByTail: MutableSet<Pair<Int, Int>>,
        tailMoves: List<String>
    ): Triple<
            Pair<Pair<Int, Int>, Pair<Int, Int>>,
            MutableSet<Pair<Int, Int>>,
            List<String>> {
        val newHeadPosition = when (move) {
            "R" -> head.first + 1 to head.second
            "L" -> head.first - 1 to head.second
            "U" -> head.first to head.second + 1
            "D" -> head.first to head.second - 1
            // step 2 moves
            "0" -> head
            "UR" -> head.first + 1 to head.second + 1
            "UL" -> head.first - 1 to head.second + 1
            "DR" -> head.first + 1 to head.second - 1
            "DL" -> head.first - 1 to head.second - 1
            else -> 0 to 0
        }
        val newTailPosition = if (!headAndTailTouch(newHeadPosition, tail)) {
            when {
                newHeadPosition.first == tail.first ->
                    newHeadPosition.first to sequenceOf(newHeadPosition.second, tail.second).average().toInt()
                newHeadPosition.second == tail.second ->
                    sequenceOf(newHeadPosition.first, tail.first).average().toInt() to tail.second
                else -> head
            }
        } else {
            tail
        }
        visitedByTail.add(tail)
        visitedByTail.add(newTailPosition)
        return Triple((newHeadPosition to newTailPosition), visitedByTail,
            tailMoves + tailsLastMove(tail, newTailPosition)
        )
    }

    private fun tailsLastMove(tail: Pair<Int, Int>, newTailPosition: Pair<Int, Int>): List<String> {
        if (tail == newTailPosition) {
            return listOf("0")
        } else if (newTailPosition.first == tail.first) {
            return if (newTailPosition.second > tail.second) listOf("U") else listOf("D")
        } else if (newTailPosition.second == tail.second) {
            return if (newTailPosition.first > tail.first) listOf("R") else listOf("L")
        } else if (newTailPosition.first > tail.first) {
            return if (newTailPosition.second > tail.second) listOf("UR") else listOf("DR")
        } else if (newTailPosition.first < tail.first) {
            return if (newTailPosition.second > tail.second) listOf("UL") else listOf("DL")
        }
        return listOf()
    }

    private fun headAndTailTouch(head: Pair<Int, Int>, tail: Pair<Int, Int>) =
        abs(head.first - tail.first) <= 1 && abs(head.second - tail.second) <= 1

    private fun step2(): Int {
        var curPosHead: Pair<Int, Int>
        var curPosTail: Pair<Int, Int>
        var moves = readFile("day9med.txt", 2022)
            .map { it.split(' ').zipWithNext().single() }
            .map { it.first to (0 until it.second.toInt()) }
            .flatMap { stringAndRange -> stringAndRange.second.map { stringAndRange.first } }
        var tailMoves: List<String>
        var result = 0
        repeat(9) {
            curPosHead = 0 to 0
            curPosTail = 0 to 0
            tailMoves = listOf()
            val visited = mutableSetOf<Pair<Int, Int>>()
            moves.forEach {
                val result = move(curPosHead, curPosTail, it, visited, tailMoves)
                curPosHead = result.first.first
                curPosTail = result.first.second
                tailMoves = result.third
            }
            moves = tailMoves
            result = visited.size
            println("$ $result")
        }
        return result
    }
}
