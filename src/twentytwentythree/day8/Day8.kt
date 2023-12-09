package twentytwentythree.day8

import readFile
import kotlin.math.max

class Day8 {
    fun run() {
        val input = readFile("day8.txt", 2023)
        val network = parse(input)
        println(network.followPath())
        println(network.followPathFromAtoZ())
    }

    private fun parse(input: List<String>): Network =
            Network(input[0], input.drop(2).associate {
                it.substring(0, 3) to Choice(
                        it.substring(7, 10),
                        it.substring(12, 15))
            })
}

data class Network(
        val instructions: String,
        val nodes: Map<String, Choice>
) {
    fun followPath() =
        generateSequence("AAA" to 0L) { nodes[it.first]!!.choose(instructions.modGet(it.second)) to it.second + 1 }
            .first { "ZZZ" == it.first }
            .second

    fun followPathFromAtoZ(): Long = nodes.keys.filter { it[2] == 'A' }
        .map { followPathWithStartingNodeAndStopsWhenTrailingZ(it) }
        .map { it.second }
        .let { lcm(it) }

    private fun followPathWithStartingNodeAndStopsWhenTrailingZ(startingNode: String) =
        generateSequence(startingNode to 0L) { nodes[it.first]!!.choose(instructions.modGet(it.second)) to it.second + 1 }
            .first { 'Z' == it.first[2] }
}

data class Choice(
        val left: String,
        val right: String
) {
    fun choose(c: Char) = if (c == 'L') left else right
}

fun String.modGet(n: Long): Char = this[((n % this.length).toInt())]

private fun lcm(numbers: List<Long>): Long =
    numbers.reduce { acc, l ->
        (max(acc, l)).let { larger ->
            generateSequence(larger) { it + larger }.dropWhile { it % acc != 0L || it % l != 0L }.first()
        }
    }
