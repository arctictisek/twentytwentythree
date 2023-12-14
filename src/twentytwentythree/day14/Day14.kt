package twentytwentythree.day14

import readFile

class Day14 {
    fun run() {
        val input = readFile("day14.txt", 2023)

        val cycles = input.doNcycles(1000)

        cycles.transpose().sumOf { it.computeWeight() }.let { println(it) }
    }

    private fun List<String>.doOneCycle(): List<String> {
        val shakenNorth = this.transpose().map { it.toMutableList() }
                .map { it.shakeAllTheWayLeft() }
                .map { it.joinToString("") }.transpose()
        val shakenWest = shakenNorth.map { it.toMutableList() }
                .map { it.shakeAllTheWayLeft() }
                .map { it.joinToString("") }
        val shakenSouth = shakenWest.transpose().map { it.toMutableList() }
                .map { it.shakeAllTheWayRight() }
                .map { it.joinToString("") }.transpose()
        val shakenEast = shakenSouth.map { it.toMutableList() }
                .map { it.shakeAllTheWayRight() }
                .map { it.joinToString("") }
        return shakenEast
    }

    private tailrec fun List<String>.doNcycles(n: Int): List<String> =
            if (n == 0) this else this.doOneCycle().doNcycles(n - 1)

    private fun List<String>.transpose() = (0..<this[0].length).map { curIndex ->
        this.map { it[curIndex] }.joinToString("")
    }

    private fun MutableList<Char>.shiftOneLeft(): MutableList<Char> {
        (1..<size).forEach {
            if (this[it] == 'O' && this[it - 1] == '.') {
                this[it - 1] = 'O'
                this[it] = '.'
                return this
            }
        }
        return this
    }

    private fun MutableList<Char>.shiftOneRight(): MutableList<Char> {
        (1..<size).forEach {
            if (this[it] == '.' && this[it - 1] == 'O') {
                this[it] = 'O'
                this[it - 1] = '.'
                return this
            }
        }
        return this
    }

    private tailrec fun MutableList<Char>.shakeAllTheWayLeft(): MutableList<Char> {
        val orig = this.joinToString("")
        val updated = this.shiftOneLeft()
        return if (orig == updated.joinToString("")) this else updated.shakeAllTheWayLeft()
    }

    private tailrec fun MutableList<Char>.shakeAllTheWayRight(): MutableList<Char> {
        val orig = this.joinToString("")
        val updated = this.shiftOneRight()
        return if (orig == updated.joinToString("")) this else updated.shakeAllTheWayRight()
    }

    private fun String.computeWeight(): Int = this.mapIndexed { index, c -> if (c == 'O') length - index else 0 }.sum()
}
