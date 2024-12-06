package twentytwentyfour.day5

import p
import readFile

class Day5 {
    fun run() {
        val input = readFile("day5Simple.txt", 2024)
        val rules = input.takeWhile { it.isNotBlank() }
            .map { it.split("|").map { it.toInt() }.zipWithNext().first() }
        val inputPages = input.dropWhile { it.isNotBlank() }
            .drop(1)
            .map { it.split(",").map { it.toInt() } }
        inputPages.filter { it.areInCorrectOrder(rules) }.sumOf { it.middle() }.p()
    }

    fun List<Int>.areInCorrectOrderForIndex(index: Int, rules: List<Pair<Int, Int>>): Boolean {
        val cur = this[index]
        val pairsBefore = this.take(index).map { it to cur }
        val pairsAfter = this.drop(index + 1).map { cur to it }
        return pairsBefore.none { it.pairBreaksAnyRule(rules) } && pairsAfter.none { it.pairBreaksAnyRule(rules) }
    }

    fun List<Int>.areInCorrectOrder(rules: List<Pair<Int, Int>>) =
        this.indices.all { this.areInCorrectOrderForIndex(it, rules) }

    fun Pair<Int, Int>.pairBreaksAnyRule(rules: List<Pair<Int, Int>>) = rules.any { it == this.flip() }

    fun Pair<Any, Any>.flip(): Pair<Any, Any> = this.second to this.first

    fun List<Int>.middle() = this[this.size / 2]

    fun List<Int>.swap(a: Int, b: Int): List<Int> {
        val index1 = indexOf(a)
        val index2 = indexOf(b)

        return mapIndexed { index, value ->
            when (index) {
                index1 -> b
                index2 -> a
                else -> value
            }
        }
    }
}
