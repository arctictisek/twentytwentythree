package twentytwentythree.day13

import readFileToString

class Day13 {
    fun run() {
        val input = readFileToString("day13.txt", 2023)
        val patterns = input.split("\n\n")
                .map { it.split("\n") }

        println("plouf")
        "xxx".symetricalOver(4)
    }

    private fun String.symetricalOver(n: Int): Boolean {
        (0..<3).forEach { println(it) }
        return true
    }
}
