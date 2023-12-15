package twentytwentythree.day15

import readFileToString

class Day15 {
    fun run() {
        val input = readFileToString("day15.txt", 2023)
        val result = input.trim().computeHashOfInitialisationSequence()
        println(result)
    }

    private fun Char.hashValue(currentValue: Int = 0) =
        ((currentValue + this.code) * 17) % 256

    private fun String.hashValue() = this.fold(0) { acc, c -> c.hashValue(acc) }

    private fun String.computeHashOfInitialisationSequence() = this.split(',').sumOf { it.hashValue() }
}
