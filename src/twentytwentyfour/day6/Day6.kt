package twentytwentyfour.day6

import p
import readFile

class Day6 {
    fun run() {
        val input = readFile( "day6Simple.txt", 2024)
        parseInput(input).p()
    }

    fun parseInput(input: List<String>): Pair<List<Pair<Int, Int>>, Pair<Int, Int>> {
        val obs = input.indices
            .mapNotNull { y ->
                input[y].indices.filter { x -> input[y][x] == '#' }.map { it to y }
            }
            .flatten()
        val guardY = input.indices.find { input[it].contains('^') }!!
        val guardX = input[guardY].indices.find { input[guardY][it] == '^' }!!
        return obs to (guardY to guardX)
    }

    enum class Direction(private val factors: Pair<Int, Int>) {
        LEFT(-1 to 0), RIGHT(1 to 0), UP(0 to -1), DOWN(0 to 1);
    }
}
