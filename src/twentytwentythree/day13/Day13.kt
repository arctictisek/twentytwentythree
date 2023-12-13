package twentytwentythree.day13

import readFileToString

class Day13 {
    fun run() {
//        val input = readFileToString("day13.txt", 2023)
        val input = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
        """.trimIndent()
        val patterns = input.split("\n\n")
                .map { it.split("\n") }
        println(cols(patterns[0]))
    }
    private fun String.safeGet(n: Int) =
        if (n < length) this[n] else '%'
    private fun List<String>.safeGet(n: Int) = if (n < this.size) this[n] 
    private fun String.symmetricalOver(n: Int) =
        (1..<n).all { this.safeGet(n - it) == this.safeGet(n + it - 1) }

    private fun cols(pattern: List<String>): Int = (2..<pattern[0].length).first { symmetryIndex ->
        pattern.all { it.symmetricalOver(symmetryIndex) }
    } - 1

    private fun rows(pattern: List<String>) =
        (2..<pattern.size).first {
            symmetricalOverI(pattern, it)
        } - 1

    private fun symmetricalOverI(pattern: List<String>, i: Int) =
        (1..<i).all { pattern[i - it] == pattern[i + it - 1] }
}
