package twentytwentytwo.day9

import readFile
import kotlin.math.abs

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
        val moves = readFile("day9.txt")
            .map { it.split(' ').zipWithNext().single() }
            .joinToString(separator = "") { it.first.repeat(it.second.toInt()) }
        moves.forEach {
            val result = move(curPosHead, curPosTail, it, visited)
            curPosHead = result.first.first
            curPosTail = result.first.second
        }
        return visited.size
    }

    private fun move(
        head: Pair<Int, Int>,
        tail: Pair<Int, Int>,
        move: Char,
        visitedByTail: MutableSet<Pair<Int, Int>>
    ): Pair<
            Pair<Pair<Int, Int>, Pair<Int, Int>>,
            MutableSet<Pair<Int, Int>>> {
        val newHeadPosition = when (move) {
            'R' -> head.first + 1 to head.second
            'L' -> head.first - 1 to head.second
            'U' -> head.first to head.second + 1
            'D' -> head.first to head.second - 1
            else -> 0 to 0
        }
        val newTailPosition = if (!headAndTailTouch(newHeadPosition, tail)) {
            if (newHeadPosition.first == tail.first) {
                newHeadPosition.first to sequenceOf(newHeadPosition.second, tail.second).average().toInt()
            } else if (newHeadPosition.second == tail.second) {
                sequenceOf(newHeadPosition.first, tail.first).average().toInt() to tail.second
            } else {
                head
            }
        } else {
            tail
        }
        visitedByTail.add(tail)
        visitedByTail.add(newTailPosition)
        return (newHeadPosition to newTailPosition) to visitedByTail
    }

    private fun headAndTailTouch(head: Pair<Int, Int>, tail: Pair<Int, Int>) =
        abs(head.first - tail.first) <= 1 && abs(head.second - tail.second) <= 1

    private fun step2() = 0
}
