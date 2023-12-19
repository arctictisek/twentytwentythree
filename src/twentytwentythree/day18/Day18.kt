package twentytwentythree.day18

import readFile
import kotlin.math.max
import kotlin.math.min

class Day18 {
    fun run() {
        val input = readFile("day18Simple.txt", 2023)
        val lines = parse(input)
        val result = lines.fold(listOf(Coordinates(0, 0))) { acc, currentLine ->
            acc + addPathForLine(currentLine, acc.last())
        }
//        val newResult = lines.scan(Line(0 to 0, 0 to 0, Direction.VERTICAL)) { acc, currentLine ->
//            addLine(currentLine, acc.end)
//        }
        val newResult = lines.scan(listOf(Line(0 to 0, 0 to 0, Direction.VERTICAL))) { acc, currentLine ->
            listOf(addLine(currentLine, acc.last().end))
        }.flatten()
        render(result)
        println()
        newResult.renderLineList()
        val x = 0
//        val pointIn = findPointInside(result)
//        val improved = colourAround(listOf(pointIn) + result, pointIn)
//        render(improved)
//        println(improved.distinct().size)
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

//    private fun addLine(line: Pair<Char, Int>, start: Coordinates): Line =
//        when (line.first) {
//            'R' -> Line(
//                Coordinates(start.x + 1, start.y),
//                Coordinates(start.x + line.second, start.y),
//                Direction.HORIZONTAL
//            )
//
//            'D' -> Line(
//                Coordinates(start.x, start.y + 1),
//                Coordinates(start.x, start.y + line.second),
//                Direction.VERTICAL
//            )
//
//            'L' -> Line(
//                Coordinates(start.x - 1, start.y),
//                Coordinates(start.x - line.second, start.y),
//                Direction.HORIZONTAL
//            )
//
//            'U' -> Line(
//                Coordinates(start.x, start.y - 1),
//                Coordinates(start.x, start.y - line.second),
//                Direction.VERTICAL
//            )
//
//            else -> Line(Coordinates(0, 0), Coordinates(0, 0), Direction.VERTICAL)
//        }

    private fun addLine(line: Pair<Char, Int>, start: Coordinates): Line =
        when (line.first) {
            'R' -> Line(
                Coordinates(start.x + 1, start.y),
                Coordinates(start.x + line.second, start.y),
                Direction.HORIZONTAL
            )

            'D' -> Line(
                Coordinates(start.x, start.y + 1),
                Coordinates(start.x, start.y + line.second),
                Direction.VERTICAL
            )

            'L' -> Line(
                Coordinates(start.x - 1, start.y),
                Coordinates(start.x - line.second, start.y),
                Direction.HORIZONTAL
            )

            'U' -> Line(
                Coordinates(start.x, start.y - 1),
                Coordinates(start.x, start.y - line.second),
                Direction.VERTICAL
            )

            else -> Line(Coordinates(0, 0), Coordinates(0, 0), Direction.VERTICAL)
        }


    private fun isPointOnALine(point: Coordinates, lines: List<Line>) =
        lines.any {
            (it.direction == Direction.VERTICAL && it.start.x == point.x && it.getYRange().contains(point.y)) ||
                    (it.direction == Direction.HORIZONTAL && it.start.y == point.y && it.getXRange().contains(point.x))
        }

    private fun isPointInsideArea(point: Coordinates, lines: List<Line>) =
        isPointOnALine(point, lines) ||
                (lines.filter { it.direction == Direction.VERTICAL }
                    .filter { it.start.x < point.x }
                    .count { it.getYRange().contains(point.y) } % 2 == 1 &&
                        lines.filter { it.direction == Direction.VERTICAL }
                            .filter { it.start.x > point.x }
                            .count { it.getYRange().contains(point.y) } % 2 == 1 &&
                        lines.filter { it.direction == Direction.HORIZONTAL }
                            .filter { it.start.y < point.y }
                            .count { it.getXRange().contains(point.x) } % 2 == 1 &&
                        lines.filter { it.direction == Direction.HORIZONTAL }
                            .filter { it.start.y > point.y }
                            .count { it.getXRange().contains(point.x) } % 2 == 1)

    private fun List<Line>.minXY() =
        this.minOf { min(it.start.x, it.end.x) } to this.minOf { min(it.start.y, it.end.y) }

    private fun List<Line>.maxXY() =
        this.maxOf { max(it.start.x, it.end.x) } to this.maxOf { max(it.start.y, it.end.y) }

    private fun List<Line>.renderLineList() {
        val minXY = minXY()
        val maxXY = maxXY()
        (minXY.second..maxXY.second).forEach { y ->
            (minXY.first..maxXY.first).forEach { x ->
                if (isPointInsideArea(Coordinates(x, y), this)) print('#') else print('.')
            }
            println()
        }
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

data class Line(
    val start: Coordinates,
    val end: Coordinates,
    val direction: Direction
) {
    constructor(start: Pair<Int, Int>, end: Pair<Int, Int>, direction: Direction) :
            this(Coordinates(start.first, start.second), Coordinates(end.first, end.second), direction)

    fun getXRange() = getRange(start.x, end.x)
    fun getYRange() = getRange(start.y, end.y)
}

enum class Direction {
    HORIZONTAL, VERTICAL
}

private fun getRange(i: Int, j: Int) = if (i < j) (i..j) else (j..i)
