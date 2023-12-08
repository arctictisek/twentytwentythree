package twentytwentythree.day8

import readFile
import kotlin.math.max

class Day8 {
    fun run() {
        val input = readFile("2023-day8.txt")
        val network = parse(input)
        println(network.followPath())
        network.nodes.keys.filter { it[2] == 'A' }
                .map { network.followPathWithStartingNodeAndStopsWhenTrailingZ(it).second }
                .let { println(lcm(it)) }
    }

    private fun lcm(numbers: List<Long>): Long =
            numbers.reduce { acc, l ->
                (max(acc, l)).let { larger ->
                    generateSequence(larger) { it + larger }.dropWhile { it % acc != 0L || it % l != 0L }.first()
                }
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
    fun followPath(): Long {
        var curNode = "AAA"
        var curStep = 0L
        while (curNode != "ZZZ") {
            curNode = nodes[curNode]!!.choose(instructions.modGet(curStep))
            ++curStep
        }
        return curStep
    }

    fun followPathWithStartingNodeAndStopsWhenTrailingZ(startingNode: String): Pair<String, Long> {
        var curNode = startingNode
        var curStep = 0L
        while (curNode[2] != 'Z') {
            curNode = nodes[curNode]!!.choose(instructions.modGet(curStep))
            ++curStep
        }
        return curNode to curStep
    }
}

data class Choice(
        val left: String,
        val right: String
) {
    fun choose(c: Char) = if ('L' == c) left else right
}

fun String.modGet(n: Long): Char = this[((n % this.length).toInt())]
