package twentytwentythree.day14

import readFile

class Day14 {
    fun run() {
        val input = readFile("day14.txt", 2023)
        val lines = input.transpose().map { it.toMutableList() }
        val shakenLines = lines.map { it.shakeAllTheWay() }.map { it.joinToString("") }
        shakenLines.sumOf { it.computeWeight() }.let { println(it) }

    }

    private fun List<String>.transpose() = (0..<this[0].length).map { curIndex ->
        this.map { it[curIndex] }.joinToString("")
    }

    private fun MutableList<Char>.shiftOneLeft(): MutableList<Char> {
        (1..<size).forEach {
            if (this[it] == 'O' && this [it - 1] == '.') {
                this[it - 1] = 'O'
                this[it] = '.'
                return this
            }
        }
        return this
    }

    private tailrec fun MutableList<Char>.shakeAllTheWay(): MutableList<Char> {
        val orig = this.joinToString("")
        val updated = this.shiftOneLeft()
        return if (orig == updated.joinToString("")) this else updated.shakeAllTheWay()
    }

    private fun String.computeWeight(): Int = this.mapIndexed { index, c -> if (c == 'O') length - index else 0 }.sum()
}
