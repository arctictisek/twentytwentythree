package twentytwentyfour.day3

import p
import readFile

class Day3 {
    fun run() {
        val input = readFile("day3.txt", 2024)
        parse(input.joinToString("")).sumOf { it.first * it.second }.p()
        parse2(input.joinToString("")).filterList().sumOf { it.first * it.second }.p()
    }

    private fun parse(input: String) =
        Regex("""mul\(\d+,\d+\)""")
            .findAll(input)
            .map { it.value }
            .map { it.substring(4) }
            .map { it.dropLast(1) }
            .map { it.split(",").zipWithNext().first() }
            .map { it.first.toInt() to it.second.toInt() }
            .toList()

    private fun parse2(input: String) =
        Regex("""mul\(\d+,\d+\)|do\(\)|don't\(\)""").findAll(input).map { it.value }.toList()

    private fun List<String>.filterList(): List<Pair<Int, Int>> {
        val result = mutableListOf<String>()
        var dropping = false
        for (token in this) {
            when {
                token == "don't()" -> dropping = true
                token == "do()" -> dropping = false
                !dropping -> result.add(token)
            }
        }
        return result.map { it.substring(4) }
            .map { it.dropLast(1) }
            .map { it.split(",").map { it.toInt() }.zipWithNext().first() }
            .toList()
    }
}
