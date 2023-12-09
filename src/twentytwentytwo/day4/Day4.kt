package twentytwentytwo.day4

import readFile

class Day4 {
    fun run() {
        println(step1())
        println(step2())
    }

    private fun step1(): Int =
        computeRangePairs()
            .count { pair -> pair.first.all { pair.second.contains(it) } || pair.second.all { pair.first.contains(it) } }

    private fun step2(): Int =
        computeRangePairs()
            .count { pair -> pair.first.any { pair.second.contains(it) } || pair.second.any { pair.first.contains(it) } }

    private fun computeRangePairs(): Sequence<Pair<IntRange, IntRange>> = readFile("day4.txt", 2022).asSequence()
        .map { """(\d+)\-(\d+),(\d+)\-(\d+)""".toRegex().find(it)!!.groupValues }
        .map { it.drop(1) }
        .map { currentLine -> currentLine.map { it.toInt() } }
        .map { Pair(IntRange(it[0], it[1]), IntRange(it[2], it[3])) }
}
