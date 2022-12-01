package twentytwentytwo.day3

import readFile

class Day3 {
    fun run() {
        println(step1())
        println(step2())
    }

    private fun step1(): Int {
        return readFile("day3.txt")
            .asSequence()
            .filter { it.isNotBlank() }
            .map { Pair(it.substring(0, it.length / 2), it.substring(it.length / 2)) }
            .map { Pair(it.first.asSequence().toSet(), it.second.asSequence().toSet()) }
            .map { it.first intersect it.second }
            .map { it.first() }
            .sumOf { it.code - if (it.isLowerCase()) 'a'.code - 1 else 'A'.code - 27 }
    }

    private fun step2(): Int {
        return readFile("day3.txt")
            .asSequence()
            .chunked(3)
            .filter { 3 == it.size }
            .map { it[0].toSet() intersect  it[1].toSet() intersect it[2].toSet() }
            .map { it.first() }
            .sumOf { it.code - if (it.isLowerCase()) 'a'.code - 1 else 'A'.code - 27 }
    }

}
