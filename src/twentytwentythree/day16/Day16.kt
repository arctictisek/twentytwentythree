package twentytwentythree.day16

import readFile
import kotlin.math.max

class Day16 {
    fun run() {
        val input = readFile("day16.txt", 2023)
        val mirrors = parse(input)
        mirrors.render()

        energiseSpace(0 to 0, Direction.RIGHT, mirrors, input[0].length to input.size).let {
            println(it.count())
        }

        max(
            (0..<input[0].length).maxOf { x ->
                max(
                    energiseSpaceNew(
                                    x to 0,
                                    Direction.DOWN,
                                    mirrors,
                                    input[0].length to input.size
                                ).count(),
                    energiseSpaceNew(
                                    x to input.size - 1,
                                    Direction.UP,
                                    mirrors,
                                    input[0].length to input.size
                                ).count()
                )
            },
            input.indices.maxOf { y ->
                max(
                    energiseSpaceNew(
                                    0 to y,
                                    Direction.RIGHT,
                                    mirrors,
                                    input[0].length to input.size
                                ).count(),
                    energiseSpaceNew(
                                    input[0].length - 1 to y,
                                    Direction.LEFT,
                                    mirrors,
                                    input[0].length to input.size
                                ).count()
                )
            }).let { println(it) }
    }

    private fun parse(input: List<String>) =
        input.mapIndexed { y, s ->
            s.mapIndexed { x, c -> Space(x, y, c) }.filter { it.mirror != '.' }
        }.flatten()

    private fun energiseSpaceNew(
        spaceXy: Pair<Int, Int>,
        direction: Direction,
        mirrors: List<Space>,
        bounds: Pair<Int, Int>
    ): Set<Pair<Int, Int>> {
        mirrors.forEach { it.hitFrom.clear() }
        return energiseSpace(spaceXy, direction, mirrors, bounds)
    }


    private fun energiseSpace(
        spaceXy: Pair<Int, Int>,
        direction: Direction,
        mirrors: List<Space>,
        bounds: Pair<Int, Int>
    ): Set<Pair<Int, Int>> {
        if (spaceXy.isOutOfBounds(bounds)) return setOf()
        val space =
            mirrors.firstOrNull { it.x == spaceXy.first && it.y == spaceXy.second } ?: Space(
                spaceXy.first,
                spaceXy.second,
                '.'
            )
        if (space.isEmpty()) {
            val toEnergise = mutableSetOf(space.x to space.y)
            var newSpace: Pair<Int, Int> = space.x to space.y
            while (true) {
                newSpace = newSpace.next(direction)
                if (newSpace.goThrough(direction, mirrors)) {
                    if (newSpace.isOutOfBounds(bounds)) break
                    else toEnergise.add(newSpace)
                } else break
            }
            return toEnergise + energiseSpace(newSpace, direction, mirrors, bounds)
        } else {
            if (space.mirror.goThrough(direction)) {
                return setOf(space.x to space.y) + energiseSpace(space.next(direction), direction, mirrors, bounds)
            } else if (space.mirror == '|') {
                if (space.hitFrom.contains(direction)) return setOf()
                space.hitFrom.add(direction)
                return setOf(space.x to space.y) + energiseSpace(
                    space.next(Direction.DOWN),
                    Direction.DOWN,
                    mirrors,
                    bounds
                ) +
                        energiseSpace(space.next(Direction.UP), Direction.UP, mirrors, bounds)
            } else if (space.mirror == '-') {
                if (space.hitFrom.contains(direction)) return setOf()
                space.hitFrom.add(direction)
                return setOf(space.x to space.y) + energiseSpace(
                    space.next(Direction.LEFT),
                    Direction.LEFT,
                    mirrors,
                    bounds
                ) +
                        energiseSpace(space.next(Direction.RIGHT), Direction.RIGHT, mirrors, bounds)
            } else if (space.mirror == '\\') {
                if (space.hitFrom.contains(direction)) return setOf()
                space.hitFrom.add(direction)
                space.mirror.nextSingleDirection(direction)
                    .let { newDirection ->
                        return setOf(space.x to space.y) + energiseSpace(
                            space.next(newDirection),
                            newDirection,
                            mirrors,
                            bounds
                        )
                    }
            } else if (space.mirror == '/') {
                if (space.hitFrom.contains(direction)) return setOf()
                space.hitFrom.add(direction)
                space.mirror.nextSingleDirection(direction)
                    .let { newDirection ->
                        return setOf(space.x to space.y) + energiseSpace(
                            space.next(newDirection),
                            newDirection,
                            mirrors,
                            bounds
                        )
                    }
            }
        }
        return setOf()
    }

    private fun List<Space>.render() {
        (maxOf { it.x } to maxOf { it.y }).let { (maxX, maxY) ->
            (0..maxY).forEach { y ->
                (0..maxX).forEach { x ->
                    print(findChar(x to y))
                }
                println()
            }
        }
    }

    private fun List<Pair<Int, Int>>.renderEnergised(maxX: Int, maxY: Int) {
        (0..<maxY).forEach { y ->
            (0..<maxX).forEach { x ->
                print(findEnergisedChar(x to y))
            }
            println()
        }
    }

    private fun List<Space>.findChar(xy: Pair<Int, Int>) =
        this.firstOrNull { it.x == xy.first && it.y == xy.second }?.mirror ?: '.'

    private fun List<Pair<Int, Int>>.findEnergisedChar(xy: Pair<Int, Int>) =
        if (this.any { it.first == xy.first && it.second == xy.second }) '#' else '.'

    private fun Pair<Int, Int>.isOutOfBounds(bounds: Pair<Int, Int>): Boolean =
        this.first >= bounds.first || this.second >= bounds.second || this.first < 0 || this.second < 0

    private fun Char.nextSingleDirection(direction: Direction) =
        if (this == '\\') {
            when (direction) {
                Direction.LEFT -> Direction.UP
                Direction.RIGHT -> Direction.DOWN
                Direction.DOWN -> Direction.RIGHT
                Direction.UP -> Direction.LEFT
            }
        } else {
            when (direction) {
                Direction.LEFT -> Direction.DOWN
                Direction.RIGHT -> Direction.UP
                Direction.DOWN -> Direction.LEFT
                Direction.UP -> Direction.RIGHT
            }
        }

}


data class Space(
    val x: Int,
    val y: Int,
    val mirror: Char,
    val hitFrom: MutableSet<Direction> = mutableSetOf()
) {
    fun isEmpty() = mirror == '.'
    fun next(direction: Direction) =
        (x to y).next(direction)
}

private fun Pair<Int, Int>.next(direction: Direction) =
    when (direction) {
        Direction.LEFT -> this.first - 1 to this.second
        Direction.RIGHT -> this.first + 1 to this.second
        Direction.DOWN -> this.first to this.second + 1
        Direction.UP -> this.first to this.second - 1
    }

private fun Pair<Int, Int>.goThrough(direction: Direction, mirrors: List<Space>) =
    mirrors.firstOrNull { it.x == first && it.y == second }.let { mirror ->
        null == mirror ||
                (mirror.mirror == '-' && setOf(Direction.LEFT, Direction.RIGHT).contains(direction)) ||
                (mirror.mirror == '|' && setOf(Direction.UP, Direction.DOWN).contains(direction))
    }

private fun Char.goThrough(direction: Direction) =
    when {
        this == '|' -> setOf(Direction.UP, Direction.DOWN).contains(direction)
        this == '-' -> setOf(Direction.LEFT, Direction.RIGHT).contains(direction)
        else -> false
    }

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}
