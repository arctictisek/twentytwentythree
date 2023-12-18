package twentytwentythree.day18

import readFile

class Day18 {
    fun run() {
        val input = readFile("day18.txt", 2023)
        val lines = parse(input)
        val result = lines.fold(listOf(Coordinates(0, 0))) { acc, currentLine ->
            acc + addPathForLine(currentLine, acc.last())
        }
        val pointIn = findPointInside(result)
        val improved = colourAround(listOf(pointIn) + result, pointIn)
//        render(improved)
        println(improved.distinct().size)
    }

    private fun parse(input: List<String>) =
        input.map { it.split(' ') }
            .map { it[0][0] to it[1].toInt() }

    private fun addPathForLine(line: Pair<Char, Int>, start: Coordinates) =
        if (line.first == 'R') {
            (start.x + 1..start.x + line.second).map { x -> Coordinates(x, start.y) }
        } else if (line.first == 'D') {
            (start.y + 1..start.y + line.second).map { y -> Coordinates(start.x, y) }
        } else if (line.first == 'L') {
            (start.x - line.second..<start.x).reversed().map { x -> Coordinates(x, start.y) }
        } else { // 'U'
            (start.y - line.second..<start.y).reversed().map { y -> Coordinates(start.x, y) }
        }

    private fun render(points: List<Coordinates>) {
        (points.minOf { it.y } to points.maxOf { it.y }).let { (minY, maxY) ->
            (points.minOf { it.x } to points.maxOf { it.x }).let { (minX, maxX) ->
                (minY..maxY).forEach { y ->
                    (minX..maxX).forEach { x ->
                        if (points.any { it == Coordinates(x, y) }) print('#') else print('.')
                    }
                    println()
                }
            }
        }
    }

    private fun findPointInside(points: List<Coordinates>): Coordinates {
        (points.minOf { it.y } to points.maxOf { it.y }).let { (minY, maxY) ->
            (points.minOf { it.x } to points.maxOf { it.x }).let { (minX, maxX) ->
                (minY..maxY).forEach { y ->
                    (minX..maxX).forEach { x ->
                        if (points.count { it.x < x && it.y == y } == 1 &&
                            points.count { it.x > x && it.y == y } == 1 &&
                            points.count { it.x == x && it.y < y } == 1 &&
                            points.none { it.x == x && it.y == y })
                            return Coordinates(x, y)
                    }
                }
            }
        }
        return Coordinates(Int.MAX_VALUE, Int.MAX_VALUE)
    }

    private fun colourAround(points: List<Coordinates>, pointInside: Coordinates): List<Coordinates> {
        var working = points
        val above = Coordinates(pointInside.x, pointInside.y - 1)
        if (above.y >= working.minOf { it.y } && working.none { it.x == above.x && it.y == above.y }) {
            working = colourAround(listOf(above) + working, above)
        }
        val under = Coordinates(pointInside.x, pointInside.y + 1)
        if (under.y <= working.maxOf { it.y } && working.none { it.x == under.x && it.y == under.y }) {
            working = colourAround(listOf(under) + working, under)
        }
        val right = Coordinates(pointInside.x + 1, pointInside.y)
        if (right.x <= working.maxOf { it.x } && working.none { it.x == right.x && it.y == right.y }) {
            working = colourAround(listOf(right) + working, right)
        }
        val left = Coordinates(pointInside.x - 1, pointInside.y)
        if (left.x >= working.minOf { it.x } && working.none { it.x == left.x && it.y == left.y }) {
            working = colourAround(listOf(left) + working, left)
        }
        return working
    }
}

data class Coordinates(
    val x: Int,
    val y: Int
)
