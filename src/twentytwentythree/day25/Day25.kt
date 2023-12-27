package twentytwentythree.day25

import readFile
import kotlin.random.Random

class Day25 {
    fun run() {
        val input = readFile("day25.txt", 2023)
        val graph = parse(input)

        val result = graph.minContract()
        result.computeResult().let { println(it) }
    }

    private tailrec fun List<Pair<String, String>>.minContract(): List<Pair<String, String>> {
        val result = this.runContraction()
        return if (result.numberOfVertices() == 2 && result.size == 3) result else minContract()
    }

    private tailrec fun List<Pair<String, String>>.runContraction(): List<Pair<String, String>> {
        val contracted = this.contract()
        return if (contracted == this) this else contracted.runContraction()
    }

    private fun List<Pair<String, String>>.computeResult() = (first().first.length / 3) * (first().second.length / 3)

    private fun parse(input: List<String>) = input.map { line -> line.split(':') }
            .map { it[0] to it[1].trim().split(' ') }
            .flatMap { (left, right) -> right.map { left to it } }

    private fun List<Pair<String, String>>.contract() =
            if (this.numberOfVertices() == 2) this
            else {
                val toContract = this[Random.nextInt(this.size)]
                val contracted = toContract.contract()
                this.filter { it.isCompletelyDifferentTo(toContract) }
                        .map { edge ->
                            if (edge.first in toContract.toList()) (contracted to edge.second)
                            else if (edge.second in toContract.toList()) (edge.first to contracted)
                            else edge
                        }
                        .filter { !it.loopsBack() }
            }

}

private fun <A> Pair<A, A>.loopsBack() = first == second
private fun Pair<String, String>.contract() = if (first < second) first + second else second + first
private fun <A> Pair<A, A>.toList() = listOf(first, second)
private fun List<Pair<String, String>>.numberOfVertices() = this.flatMap { it.toList() }.distinct().size
private fun Pair<String, String>.isCompletelyDifferentTo(other: Pair<String, String>) =
        !((this.first == other.first && this.second == other.second) || (this.second == other.first && this.first == other.second))
