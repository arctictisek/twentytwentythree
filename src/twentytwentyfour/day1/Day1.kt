package twentytwentyfour.day1

import p
import readFile
import kotlin.math.abs

class Day1 {
    fun run() {
        val input = readFile("day1.txt", 2024)
        val lists = getSortedLists(input)
        getDistancesSum(lists).p()
        getTotalSimilarityScore(lists).p()
    }

    private fun getSortedLists(input: List<String>): Pair<List<Int>, List<Int>> {
        val pairs = input.map { it.split("   ").zipWithNext().first() }
        val left = pairs.map { it.first.toInt() }.sorted()
        val right = pairs.map { it.second.toInt() }.sorted()
        return left to right
    }

    private fun getDistancesSum(lists: Pair<List<Int>, List<Int>>): Int {
        return (0..<lists.first.size).sumOf { abs(lists.first[it] - lists.second[it]) }
    }

    private fun getIndividualSimilarityScore(cur: Int, right: List<Int>) = right.count { it == cur } * cur

    private fun getTotalSimilarityScore(lists: Pair<List<Int>, List<Int>>) =
        lists.first.sumOf { getIndividualSimilarityScore(it, lists.second) }
}
