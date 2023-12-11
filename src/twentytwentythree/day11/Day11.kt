package twentytwentythree.day11

import readFile
import kotlin.math.abs

class Day11 {
    fun run() {
        val input = readFile("day11.txt", 2023)
        val galaxies = parse(input)
        generateGalaxyPairs(expand(galaxies, 1000000)).sumOf { distanceBetweenGalaxies(it.first, it.second) }.let { println(it) }
    }

    private fun parse(input: List<String>): List<Pair<Long, Long>> =
        input.flatMapIndexed { y, line ->
            line.indices
                .filter { line[it] == '#' }
                .map { it.toLong() to y.toLong() }
        }

    private fun expand(galaxies: List<Pair<Long, Long>>, factor: Long): List<Pair<Long, Long>> {
        val populatedLines = galaxies.map { it.second }.distinct()
        val populatedColumns = galaxies.map { it.first }.distinct()
        val emptyLines = (0..galaxies.maxOf { it.second }).asSequence()
            .filter { !populatedLines.contains(it) }
            .toSet()
        val emptyColumns =
            (0..galaxies.maxOf { it.first }).asSequence()
                .filter { !populatedColumns.contains(it) }
                .toSet()
        return galaxies.map { galaxy ->
            galaxy.first + (factor - 1) * emptyColumns.count { it < galaxy.first } to galaxy.second + (factor - 1) * emptyLines.count { it < galaxy.second }
        }
    }

    private fun generateGalaxyPairs(galaxies: List<Pair<Long, Long>>): List<Pair<Pair<Long, Long>, Pair<Long, Long>>> =
        galaxies.flatMap { i ->
            galaxies.map { j ->
                if (i != j) setOf(i, j) else setOf()
            }
        }
            .filter { it.isNotEmpty() }
            .distinct()
            .map { it.first() to it.last() }

    private fun distanceBetweenGalaxies(galaxy1: Pair<Long, Long>, galaxy2: Pair<Long, Long>) =
        abs(galaxy2.first - galaxy1.first) + abs(galaxy2.second - galaxy1.second)
}
