package twentytwentythree.day13

import readFileToString
import kotlin.math.min

class Day13 {
    fun run() {
        val input = readFileToString("day13.txt", 2023)
        val patterns = input.split("\n\n")
                .map { it.split("\n").filter { it.isNotBlank() } }

        patterns.map { computeNotes(it) }.sum().let { println(it) }

    }

    private fun generateStringHorizontal(base: String, i: Int): Pair<Int, String> {
        val minOtlOtr = min(i, base.length - i)
        return i to base.substring(i - minOtlOtr, i + minOtlOtr)
    }

    private fun findFoldingPatterns(pattern: String): Set<Int> =
        (1..<pattern.length).map { generateStringHorizontal(pattern, it) }
                .map { it.second.substring(0, it.second.length / 2) to it.second.substring(it.second.length / 2) to it.first }
                .filter { it.first.first == it.first.second.reversed() }
                .map { it.second }
                .toSet()

    private fun findAllFoldingPatternsV(patterns: List<String>): Set<Int> = patterns.map { findFoldingPatterns(it) }
            .reduce { acc, ints -> acc.intersect(ints) }

    private fun transpose(strings: List<String>): List<String> = (0..<strings[0].length).map { curIndex ->
        strings.map { it[curIndex] }.joinToString("")
    }

    private fun computeNotes(pattern: List<String>): Int {
        findAllFoldingPatternsV(pattern).let { horizontal ->
            if (horizontal.isNotEmpty()) return horizontal.first()
            else return 100 * findAllFoldingPatternsV(transpose(pattern)).first()
        }
    }
}
