package twentytwentyfour.day2

import p
import readFile
import kotlin.math.abs

class Day2 {
    fun run() {
        val input = readFile("day2.txt", 2024)
        parseInput(input).count { checkInputOrder(it) && checkInputStep(it) || it.allButOne().any { checkInputOrder(it) && checkInputStep(it) } }
            .p()
    }

    private fun parseInput(input: List<String>) = input.map { line -> line.split(" ").map { it.toInt() } }

    private fun checkInputOrder(line: List<Int>) =
        when {
            line.first() > line[1] -> line.sorted().reversed() == line
            line.first() < line[1] -> line.sorted() == line
            else -> false
        }

    private fun checkInputStep(line: List<Int>) =
        line.windowed(2).all { abs(it[0] - it[1]) in 1..3 }

    private fun List<Int>.allButOne(): List<List<Int>> {
        return indices.map { i ->
            filterIndexed { index, _ -> index != i }
        }
    }

}
