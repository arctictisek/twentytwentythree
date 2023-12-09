package twentytwentythree.day9

import readFile

class Day9 {
    fun run() {
        val input = readFile("day9.txt", 2023).let { parse(it) }
        input.sumOf { extrapolateFullRow(it) }.let { println(it) }
        input.sumOf { alternateFullRowExtrapolation(it) }.let { println(it) }
    }

    private fun parse(input: List<String>) = input.map {
        it.split(" ").map { it.trim() }.map { it.toInt() }
    }

    private fun nextSequence(sequence: List<Int>): List<Int> =
        sequence.zipWithNext().map { it.second - it.first }

    private fun allSequences(sequence: List<Int>) =
        generateSequence(sequence) { nextSequence(it) }.takeWhile { !it.all { it == 0 } }.toList().reversed()

    private fun extrapolateEntry(sequence: List<Int>, lastOfLowerSequence: Int) =
        sequence.last() + lastOfLowerSequence

    private fun extrapolateFullRow(sequence: List<Int>) =
        allSequences(sequence).fold(0) { acc, currentSequence -> extrapolateEntry(currentSequence, acc) }

    private fun alternateEntryExtrapolation(sequence: List<Int>, firstOfLowerSequence: Int) =
        sequence.first() - firstOfLowerSequence

    private fun alternateFullRowExtrapolation(sequence: List<Int>) =
        allSequences(sequence).fold(0) { acc, currentSequence -> alternateEntryExtrapolation(currentSequence, acc) }

}
