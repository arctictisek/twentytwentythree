package twentytwentytwo.day14

import readFile

class Day14 {
    fun run() {
        println(step1())
        println(step2())
    }

    private fun step1(): Int {
        val i = readFile("day14Simple.txt")
            .map { it.split(" -> ") }
            .map { it.map { it.split(",").zipWithNext().single() } }
        return 0
    }

    private fun step2() = 0
}
