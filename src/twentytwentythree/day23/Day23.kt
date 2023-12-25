package twentytwentythree.day23

import readFile
import kotlin.math.abs

class Day23 {
    fun run() {
        val input = readFile("day23.txt", 2023)
        val tiles = parse(input)
        val start = tiles.first { it.y == 0 }
        val end = tiles.maxBy { it.y }
        val visited = mutableListOf<Tile>()
        val currentPath = mutableListOf<Tile>()
        val allPaths = mutableListOf<MutableList<Tile>>()
        listAllPaths(start, end, tiles, visited, currentPath, allPaths)
        allPaths.map { it.size }.max().let { println(it - 1) }
    }

    private fun parse(input: List<String>) =
            input.mapIndexed { y, line ->
                line.indices.filter { x -> input[y][x].isAccessibleTile() }
                        .map { x -> Tile(x, y, input[y][x]) }
            }.flatten()

    private fun listAllPaths(origin: Tile,
                             destination: Tile,
                             tiles: List<Tile>,
                             visited: MutableList<Tile>,
                             currentPath: MutableList<Tile>,
                             allPaths: MutableList<MutableList<Tile>>) {
        if (visited.contains(origin)) return
        visited.add(origin)
        currentPath.add(origin)
        if (origin == destination) {
            if (currentPath.size > (allPaths.maxOfOrNull { it.size } ?: 0))
                allPaths.add(currentPath.toMutableList())
            visited.remove(origin)
            currentPath.removeLast()
//            if (allPaths.isNotEmpty()) println(allPaths.maxOf { it.size })
            return
        }
        if (tiles.contains(origin)) {
            tiles.first { it == origin }.let { it.possibleNextIgnoreSlopes(tiles) }
                    .forEach { listAllPaths(it, destination, tiles, visited, currentPath, allPaths) }
        }
        currentPath.removeLast()
        visited.remove(origin)
    }

}

data class Tile(
        val x: Int,
        val y: Int,
        val isSlope: Boolean = false,
        val slopeDirection: Direction? = null
) {
    constructor(x: Int, y: Int, tileChar: Char) : this(x, y, tileChar.tileType() == TileType.SLOPE, tileChar.slopeDirection())

    fun possibleNext(tiles: List<Tile>) = if (isSlope) {
        when (slopeDirection) {
            Direction.LEFT -> listOf(tiles.first { it.x == x - 1 && it.y == y })
            Direction.RIGHT -> listOf(tiles.first { it.x == x + 1 && it.y == y })
            Direction.UP -> listOf(tiles.first { it.x == x && it.y == y - 1 })
            Direction.DOWN -> listOf(tiles.first { it.x == x && it.y == y + 1 })
            else -> throw Exception()
        }
    } else {
        listOfNotNull(
                tiles.firstOrNull { it.x == x + 1 && it.y == y },
                tiles.firstOrNull { it.x == x - 1 && it.y == y },
                tiles.firstOrNull { it.x == x && it.y == y + 1 },
                tiles.firstOrNull { it.x == x && it.y == y - 1 },
        )
    }

    fun possibleNextIgnoreSlopes(tiles: List<Tile>) = listOfNotNull(
            tiles.firstOrNull { it.x == x + 1 && it.y == y },
            tiles.firstOrNull { it.x == x - 1 && it.y == y },
            tiles.firstOrNull { it.x == x && it.y == y + 1 },
            tiles.firstOrNull { it.x == x && it.y == y - 1 },
    )

    fun hasHNeighbour(tiles: List<Tile>) = tiles.any { it.y == y && 1 == abs(it.x - x) }
    fun hasVNeighbour(tiles: List<Tile>) = tiles.any { it.x == x && 1 == abs(it.y - y) }
}

private fun Char.slopeDirection() = when {
    '<' == this -> Direction.LEFT
    '>' == this -> Direction.RIGHT
    '^' == this -> Direction.UP
    'v' == this -> Direction.DOWN
    else -> null
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

enum class TileType {
    PATH, FOREST, SLOPE
}

private fun Char.tileType() = when {
    this == '#' -> TileType.FOREST
    this == '.' -> TileType.PATH
    setOf('<', '>', '^', 'v').contains(this) -> TileType.SLOPE
    else -> throw Exception()
}

private fun Char.isAccessibleTile() = TileType.FOREST != tileType()
