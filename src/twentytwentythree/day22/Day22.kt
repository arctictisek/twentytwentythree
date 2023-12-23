package twentytwentythree.day22

import readFile
import kotlin.math.max
import kotlin.math.min

class Day22 {
    fun run() {
        val input = readFile("day22Simple.txt", 2023)
        val x = parse(input)
        renderX(x)
        println("~~~~")
//        renderY(x)

        var i = 1
        while (true) {
//            println(i++)
            val changes = x.count { it.lowerIfPossible(x) }
            println("$changes changes")
            if (0 == changes) break
        }

        println("%%%%%%%")
        println("%%%%%%%")
        println("%%%%%%%")

        renderX(x)
        println("~~~~")
        renderY(x)

        x.forEach {
            if (it supportsAnyOf x) println("$it supports something")
            else println("$it supports nothing")
        }

        println("~~~~")
        println("~~~~")


        val supports = x.asSequence().map { it to it.isSupportedBy(x) }
                .filter { it.second.size == 1 }
                .map { it.second }
                .distinct()
                .count()
        println(x.size - supports)

    }

    private fun parse(input: List<String>) =
            input.asSequence()
                    .map { it.split("~").take(2).zipWithNext().first() }
                    .map { it.first.split(',') to it.second.split(',') }
                    .map { it.first.map { it.toInt() } to it.second.map { it.toInt() } }
                    .map { Coordinates(it.first) to Coordinates(it.second) }
                    .map { Brick(it) }.toList()

    private fun renderX(bricks: List<Brick>) {
        val maxZ = bricks.maxOf { it.maxZ() }
        val maxX = bricks.maxOf { it.maxX() }
        (maxZ downTo 1).forEach { z ->
            (0..maxX).forEach { x ->
                if (bricks.any { (x to z) belongsToBrickXZ it }) print("#") else print(".")
            }
            println()
        }
    }

    private fun renderY(bricks: List<Brick>) {
        val maxZ = bricks.maxOf { it.maxZ() }
        val maxY = bricks.maxOf { it.maxY() }
        (maxZ downTo 1).forEach { z ->
            (0..maxY).forEach { y ->
                if (bricks.any { (y to z) belongsToBrickYZ it }) print("#") else print(".")
            }
            println()
        }
    }

    private infix fun Pair<Int, Int>.belongsToBrickXZ(brick: Brick) = first in brick.xRange() && second in brick.zRange()
    private infix fun Pair<Int, Int>.belongsToBrickYZ(brick: Brick) = first in brick.yRange() && second in brick.zRange()
}

data class Coordinates(
        val x: Int,
        val y: Int,
        var z: Int,
) {
    constructor(coords: List<Int>) : this(coords[0], coords[1], coords[2])
}

data class Brick(
        val c1: Coordinates,
        val c2: Coordinates,
) {
    constructor(coordsPair: Pair<Coordinates, Coordinates>) : this(coordsPair.first, coordsPair.second)

    fun maxX() = max(c1.x, c2.x)
    fun maxY() = max(c1.y, c2.y)
    fun maxZ() = max(c1.z, c2.z)
    private fun minZ() = min(c1.z, c2.z)
    fun zRange() = (min(c1.z, c2.z)..max(c1.z, c2.z))
    fun xRange() = (min(c1.x, c2.x)..max(c1.x, c2.x))
    fun yRange() = (min(c1.y, c2.y)..max(c1.y, c2.y))
    fun lowerIfPossible(bricks: List<Brick>): Boolean {
        if (minZ() == 1) return false
        else {
            --c1.z
            --c2.z
            if (bricks.filter { it != this }.any { it overlaps this }) {
                ++c1.z
                ++c2.z
                return false
            } else return true
        }
    }

//    infix fun overlaps(otherBrick: Brick): Boolean {
//        return (c1.x <= otherBrick.c2.x && c2.x >= otherBrick.c1.x) &&
//                (c1.y <= otherBrick.c2.y && c2.y >= otherBrick.c1.y) &&
//                (c1.z <= otherBrick.c2.z && c2.z >= otherBrick.c1.z)
//    }
    private infix fun overlaps(otherBrick: Brick) =
            xRange().intersect(otherBrick.xRange()).isNotEmpty() &&
                    yRange().intersect(otherBrick.yRange()).isNotEmpty() &&
                    zRange().intersect(otherBrick.zRange()).isNotEmpty()

    infix fun supportsAnyOf(bricks: List<Brick>): Boolean {
        val otherBricks = bricks.filter { it != this }
        ++c1.z
        ++c2.z
        val anyOverlap = otherBricks.any { it overlaps this }
        --c1.z
        --c2.z
        return anyOverlap
    }

    fun isSupportedBy(bricks: List<Brick>): List<Brick> {
        val otherBricks = bricks.filter { it != this }
        --c1.z
        --c2.z
        val overlaps = otherBricks.filter { it overlaps this }
        ++c1.z
        ++c2.z
        return overlaps
    }
}

