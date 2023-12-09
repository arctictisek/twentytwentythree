package twentytwentytwo.day8

import readFile

class Day8 {
    fun run() {
        parseInput("day8.txt").let {
            println(step1(it))
            println(step2(it))
        }
    }

    private fun step1(input: Collection<Collection<Int>>) =
        (input.indices)
            .flatMap { x -> (input.indices).map { x to it } }
            .count { isVisible(input, it) }

    private fun step2(input: Collection<Collection<Int>>) =
        (input.indices)
            .flatMap { x -> (input.indices).map { x to it } }
            .maxOfOrNull { computeScenicScore(input, it) }!!

    private fun isVisible(input: Collection<Collection<Int>>, coordinates: Pair<Int, Int>) =
        isOnEdge(input, coordinates.first, coordinates.second) ||
                isVisibleFromLeft(input, coordinates.first, coordinates.second) ||
                isVisibleFromRight(input, coordinates.first, coordinates.second) ||
                isVisibleFromTop(input, coordinates.first, coordinates.second) ||
                isVisibleFromBottom(input, coordinates.first, coordinates.second)

    private fun isOnEdge(input: Collection<Collection<Int>>, x: Int, y: Int) =
        x == 0 || x == input.size - 1 || y == 0 || y == input.size - 1

    private fun isVisibleFromLeft(input: Collection<Collection<Int>>, x: Int, y: Int) =
        input.elementAt(x).filterIndexed { curY, _ -> curY < y }.all { it < input.elementAt(x).elementAt(y) }

    private fun isVisibleFromRight(input: Collection<Collection<Int>>, x: Int, y: Int) =
        input.elementAt(x).filterIndexed { curY, _ -> curY > y }.all { it < input.elementAt(x).elementAt(y) }

    private fun isVisibleFromTop(input: Collection<Collection<Int>>, x: Int, y: Int) =
        (input.indices)
            .flatMap { curX -> (input.indices).map { curX to it } }
            .filter { (_, curY) -> curY == y }
            .filter { (curX, _) -> curX < x }
            .all { input.elementAt(it.first).elementAt(it.second) < input.elementAt(x).elementAt(y) }

    private fun isVisibleFromBottom(input: Collection<Collection<Int>>, x: Int, y: Int) =
        (input.indices)
            .flatMap { curX -> (input.indices).map { curX to it } }
            .filter { (_, curY) -> curY == y }
            .filter { (curX, _) -> curX > x }
            .all { input.elementAt(it.first).elementAt(it.second) < input.elementAt(x).elementAt(y) }

    private fun parseInput(file: String): Collection<Collection<Int>> =
        readFile(file, 2022).map { line -> line.map { it.digitToInt() } }.toList()

    private fun computeScenicScore(input: Collection<Collection<Int>>, coord: Pair<Int, Int>) =
        leftRange(input, coord.first, coord.second) *
                rightRange(input, coord.first, coord.second) *
                topRange(input, coord.first, coord.second) *
                bottomRange(input, coord.first, coord.second)

    private fun leftRange(input: Collection<Collection<Int>>, x: Int, y: Int) =
        (y - 1 downTo 0)
            .takeWhile { input.elementAt(x).elementAt(it) < input.elementAt(x).elementAt(y) }
            .count()
            .let { it + if (isVisibleFromLeft(input, x, y)) 0 else 1 }

    private fun rightRange(input: Collection<Collection<Int>>, x: Int, y: Int) =
        (y + 1 until input.size)
            .takeWhile { input.elementAt(x).elementAt(it) < input.elementAt(x).elementAt(y) }
            .count()
            .let { it + if (isVisibleFromRight(input, x, y)) 0 else 1 }

    private fun topRange(input: Collection<Collection<Int>>, x: Int, y: Int) =
        (x - 1 downTo 0)
            .takeWhile { input.elementAt(it).elementAt(y) < input.elementAt(x).elementAt(y) }
            .count()
            .let { it + if (isVisibleFromTop(input, x, y)) 0 else 1 }

    private fun bottomRange(input: Collection<Collection<Int>>, x: Int, y: Int) =
        (x + 1 until input.size)
            .takeWhile { input.elementAt(it).elementAt(y) < input.elementAt(x).elementAt(y) }
            .count()
            .let { it + if (isVisibleFromBottom(input, x, y)) 0 else 1 }
}
