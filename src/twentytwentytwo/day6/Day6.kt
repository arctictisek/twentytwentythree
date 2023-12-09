package twentytwentytwo.day6

import readFile

class Day6 {
    fun run() {
        println(step1())
        println(step2())
    }

    private fun step1(): Int =
        uniqueSequenceSize(4, readFile("day6.txt", 2022).first())

    private fun step2(): Int =
        uniqueSequenceSize(14, readFile("day6.txt", 2022).first())

    private fun uniqueSequenceSize(length: Int, input: String): Int =
        length + (input.indices).dropWhile { i -> length != input.substring(i).chunkedSequence(length).first().toSet().size }.first()
}
