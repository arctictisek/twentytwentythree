package twentytwentythree.day10

import readFile

class Day10 {
    fun run() {
        val input = readFile("day10.txt", 2023)
        val pipes = parseInputToUnprocessedPipes(input)
        processPipeAndReturnNeighbourPipeToProcess(
            listOf(pipes.first { it.direction == 'S' }.coordinates),
            0,
            pipes
        )
        //renderPipes(pipes)
        println(pipes.maxOf { it.value ?: 0 })
        pipes.count { isPipeInsideLoop(it, pipes) }.let { println(it) }

    }

    private fun parseInputToUnprocessedPipes(input: List<String>): List<Pipe> =
        input.flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Pipe(c, null, x to y) }
        }

    private tailrec fun processPipeAndReturnNeighbourPipeToProcess(
        pipesCoordinates: List<Pair<Int, Int>>,
        value: Int,
        pipes: List<Pipe>
    ) {
        val nextStep = pipesCoordinates.map { findPipe(it, pipes) }.filter { it.value == null }.flatMap {
            it.value = value
            it.computeNeighbours(if (it.direction == 'S') 'F' else it.direction)
        }
        if (nextStep.isNotEmpty())
            processPipeAndReturnNeighbourPipeToProcess(nextStep, value + 1, pipes)
    }

    private fun findPipe(coordinates: Pair<Int, Int>, pipes: List<Pipe>) =
        pipes.firstOrNull { it.coordinates == coordinates } ?: Pipe('.', Int.MAX_VALUE, 0 to 0)

    private fun renderPipes(pipes: List<Pipe>) {
        pipes.groupBy { it.coordinates.second }.toSortedMap().map {
            it.value.sortedBy { it.coordinates.second }
                .map {
                    if (isPipeInsideLoop(it, pipes)) 'X' else if (it.value == null) '.' else it.direction
                }
                .joinToString("")
                .replace('0', '.')
                .let { println(it) }
        }
    }

    private fun isPipeInsideLoop(pipe: Pipe, pipes: List<Pipe>) =
        pipe.value == null &&
        setOf(
            numberOfVerticalPipesOnTheLeftIsOdd(pipe, pipes),
            numberOfVerticalPipesOnTheRightIsOdd(pipe, pipes),
            numberOfHorizontalPipesAboveIsOdd(pipe, pipes),
            numberOfHorizontalPipesUnderIsOdd(pipe, pipes)
        ).all { it }

    private fun numberOfVerticalPipesOnTheLeftIsOdd(pipe: Pipe, pipes: List<Pipe>): Boolean =
        1 == pipes.filter { null != it.value }
            .filter { it.coordinates.second == pipe.coordinates.second }
            .filter { it.coordinates.first < pipe.coordinates.first }
            .filter { setOf('|', 'S', 'L', 'J', '7', 'F').contains(it.direction) }
            .sortedBy { it.coordinates.first }
            .map { it.direction }
            .joinToString("")
            .replace("F7", "")
            .replace("S7", "")
            .replace("LJ", "")
            .replace("FJ", "|")
            .replace("SJ", "|")
            .replace("L7", "|")
            .length % 2

    private fun numberOfVerticalPipesOnTheRightIsOdd(pipe: Pipe, pipes: List<Pipe>): Boolean =
        1 == pipes.filter { null != it.value }
            .filter { it.coordinates.second == pipe.coordinates.second }
            .filter { it.coordinates.first > pipe.coordinates.first }
            .filter { setOf('|', 'S', 'L', 'J', '7', 'F').contains(it.direction) }
            .sortedBy { it.coordinates.first }
            .map { it.direction }
            .joinToString("")
            .replace("F7", "")
            .replace("S7", "")
            .replace("LJ", "")
            .replace("FJ", "|")
            .replace("SJ", "|")
            .replace("L7", "|")
            .length % 2

    private fun numberOfHorizontalPipesAboveIsOdd(pipe: Pipe, pipes: List<Pipe>): Boolean =
        1 == pipes.filter { null != it.value }
            .filter { it.coordinates.second < pipe.coordinates.second }
            .filter { it.coordinates.first == pipe.coordinates.first }
            .filter { setOf('-', 'S', 'L', 'J', '7', 'F').contains(it.direction) }
            .sortedBy { it.coordinates.first }
            .map { it.direction }
            .joinToString("")
            .replace("FL", "")
            .replace("SL", "")
            .replace("7J", "")
            .replace("FJ", "-")
            .replace("SJ", "-")
            .replace("7L", "-")
            .length % 2

    private fun numberOfHorizontalPipesUnderIsOdd(pipe: Pipe, pipes: List<Pipe>): Boolean =
        1 == pipes.filter { null != it.value }
            .filter { it.coordinates.second > pipe.coordinates.second }
            .filter { it.coordinates.first == pipe.coordinates.first }
            .filter { setOf('-', 'S', 'L', 'J', '7', 'F').contains(it.direction) }
            .sortedBy { it.coordinates.first }
            .map { it.direction }
            .joinToString("")
            .replace("FL", "")
            .replace("SL", "")
            .replace("7J", "")
            .replace("FJ", "-")
            .replace("SJ", "-")
            .replace("7L", "-")
            .length % 2
}


data class Pipe(
    val direction: Char,
    var value: Int?,
    val coordinates: Pair<Int, Int>
) {
    fun computeNeighbours(realS: Char): Set<Pair<Int, Int>> =
        when (direction) {
            '-' -> setOf(coordinates.first - 1 to coordinates.second, coordinates.first + 1 to coordinates.second)
            '|' -> setOf(coordinates.first to coordinates.second - 1, coordinates.first to coordinates.second + 1)
            'L' -> setOf(coordinates.first to coordinates.second - 1, coordinates.first + 1 to coordinates.second)
            'J' -> setOf(coordinates.first to coordinates.second - 1, coordinates.first - 1 to coordinates.second)
            '7' -> setOf(coordinates.first to coordinates.second + 1, coordinates.first - 1 to coordinates.second)
            'F' -> setOf(coordinates.first to coordinates.second + 1, coordinates.first + 1 to coordinates.second)
            'S' -> Pipe(realS, null, coordinates).computeNeighbours('%')
            else -> setOf()
        }
}
