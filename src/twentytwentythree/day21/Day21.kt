package twentytwentythree.day21

import readFile
import kotlin.math.abs

class Day21 {
    fun run() {
        val input = readFile("day21.txt", 2023)
        val accessible = parse(input)
        val start = input.mapIndexed { y, line ->
            line.indices.filter { x -> line[x] ==  'S' }.map { it to y }
        }.flatten().first()

//        val oneStep = takeOneStep(accessible, setOf(Square(start)))
        val sixSteps = takeNsteps(accessible, Square(start), 64, input.first().length - 1, input.size - 1)
        render(accessible, input.first().length - 1, input.size - 1, start, sixSteps)
        println(sixSteps.size)
    }


    private fun parse(input: List<String>) =
        input.mapIndexed { y, line ->
            line.indices.filter { x -> line[x] != '#' }.map { x -> Square(x, y) }
        }.flatten()

    private fun render(garden: List<Square>, maxX: Int, maxY: Int, start: Pair<Int, Int>,
                       possiblyVisited: Set<Square>
                       ) {
        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                if (start == (x to y)) print('S') else
                if (possiblyVisited.contains(Square(x,y))) print('O') else
                if (garden.contains(Square(x, y))) print('.') else print('#')
            }
            println()
        }
    }

    private fun takeOneStep(garden: List<Square>, possibleStarters: Set<Square>) =
            possibleStarters.map { it.accessibleNeighbours(garden).toSet() }
                    .reduce { acc, squares -> acc + squares }

    private fun takeNsteps(garden: List<Square>, start: Square, steps: Int, maxX: Int, maxY: Int) =
        generateSequence(setOf(start)) {
            val s= takeOneStep(garden, it)
            render(garden, maxX, maxY, start.x to start.y, it)
            println("~~~")
            s
        }.take(steps + 1).last()
}

data class Square(
        val x: Int,
        val y: Int,
) {
    constructor(xy: Pair<Int, Int>) : this(xy.first, xy.second)

    fun accessibleNeighbours(accessible: List<Square>) =
            accessible.filter { accessibleSquare ->
                (accessibleSquare.x == x && abs(accessibleSquare.y - y) == 1) ||
                        (abs(accessibleSquare.x - x) == 1 && accessibleSquare.y == y)
            }
}
