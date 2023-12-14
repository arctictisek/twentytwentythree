package twentytwentythree.day13

import readFileToString
import kotlin.math.min

class Day13 {
    fun run() {
        val input = readFileToString("day13.txt", 2023)
        val patterns = input.split("\n\n")
                .map { it.split("\n").filter { it.isNotBlank() } }

        patterns.sumOf { computeNotes(it) }.let { println(it) }

        patterns.map { computeNotes(it) to generateAlternatePatterns(it) }
                .map { notesToAlternates ->
                    notesToAlternates.second.map { alternate ->
                        computeNotesExcludeOld(alternate, notesToAlternates.first)
                    }.filter { it != 0 }
                }.sumOf { it.max() }.let { println(it) }

    }

    private fun generateStringHorizontal(base: String, i: Int) =
            min(i, base.length - i).let { minOtlOtr ->
                i to base.substring(i - minOtlOtr, i + minOtlOtr)
            }

    private fun findFoldingPatterns(pattern: String) =
            (1..<pattern.length).asSequence().map { generateStringHorizontal(pattern, it) }
                    .map { it.second.substring(0, it.second.length / 2) to it.second.substring(it.second.length / 2) to it.first }
                    .filter { it.first.first == it.first.second.reversed() }
                    .map { it.second }
                    .toSet()

    private fun findAllFoldingPatternsV(patterns: List<String>) = patterns.map { findFoldingPatterns(it) }
            .reduce { acc, ints -> acc.intersect(ints) }

    private fun transpose(strings: List<String>) = (0..<strings[0].length).map { curIndex ->
        strings.map { it[curIndex] }.joinToString("")
    }

    private fun computeNotes(pattern: List<String>) = findAllFoldingPatternsV(pattern).let { horizontal ->
        if (horizontal.isNotEmpty()) horizontal.first()
        else 100 * findAllFoldingPatternsV(transpose(pattern)).first()
    }

    private fun computeNotesExcludeOld(pattern: List<String>, toExclude: Int) =
            findAllFoldingPatternsV(pattern).let { horizontal ->
                if (horizontal.any { it != toExclude }) horizontal.first { it != toExclude }
                else findAllFoldingPatternsV(transpose(pattern)).let { vertical ->
                    if (vertical.any { 100 * it != toExclude })
                        100 * vertical.first { 100 * it != toExclude } else 0
                }
            }

    private fun generateAlternateLines(line: String): List<String> =
            line.indices.map { line.substring(0, it) + line[it].flip() + line.substring(it + 1) }

    private fun generateAlternatePatterns(pattern: List<String>) = pattern.indices.map { curLine ->
        generateAlternateLines(pattern[curLine]).map { alternative ->
            pattern.slice(0..<curLine) + alternative + pattern.slice(curLine + 1..<pattern.size)
        }
    }.flatten()

    private fun Char.flip() = if (this == '.') '#' else '.'
}
