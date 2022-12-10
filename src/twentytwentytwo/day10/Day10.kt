package twentytwentytwo.day10

import readFile
import kotlin.math.abs

class Day10 {
    fun run() {
        println(step1())
        println(step2())
    }


    private fun step1() =
        computeCyclesAndRegister("day10.txt")
            .filter { listOf(20, 60, 100, 140, 180, 220).contains(it.first) }
            .sumOf { it.first * it.second }
    
    private fun step2(): String = computeCyclesAndRegister("day10.txt")
        .chunked(40)
        .joinToString(separator = "\n") {
            it.map { if (abs(((it.first - 1) % 40) - it.second) < 2) '#' else '.' }.joinToString(separator = "")
        }

    private fun computeCyclesAndRegister(file: String): List<Pair<Int, Int>> {
        var cycles = 0
        var register = 1
        return readFile(file)
            .map { it.split(' ') }
            .map { it + if (it[0] == "noop") listOf("0") else listOf() }
            .map { it.zipWithNext().single() }
            .map { it.first to it.second.toInt() }
            .flatMap {
                if (it.first == "noop") {
                    ++cycles
                    listOf(cycles to register)
                } else {
                    val result = listOf(cycles + 1 to register, cycles + 2 to register)
                    register += it.second
                    cycles += 2
                    result
                }
            }
    }
}
