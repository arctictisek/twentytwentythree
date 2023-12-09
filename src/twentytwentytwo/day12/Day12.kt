package twentytwentytwo.day12

import readFile
import kotlin.math.min

class Day12 {
    fun run() {
        println(step1())
        println(step2())
    }

    private fun step1(): Long {
        val i = readFile("day12.txt", 2022)
        val nodes =
            i.indices.flatMap { y -> i[y].indices.map { x -> Node(x, y, i[y][x], false, Long.MAX_VALUE) } }.toSet()
        nodes.single { i[it.y][it.x] == 'S' }
            .let { node -> nodes.single { it.x == node.x && node.y == it.y }.distance = 0 }
        val destination = nodes.single { i[it.y][it.x] == 'E' }
        destination.value = 'z' + 1
        var curNode = nodes.single { i[it.y][it.x] == 'S' }
        while (curNode != destination) {
            val neighbours = listAccessibleNeighbours(curNode, nodes)
            neighbours.forEach { neighbour ->
                getNode(neighbour.first, neighbour.second, nodes).let {
                    it.distance = min(it.distance, curNode.distance + 1)
                }
            }
            curNode.visited = true
            curNode = nodes.filter { !it.visited }.minBy { it.distance }
        }
//        printGrid(nodes)
        return destination.distance
    }
    private fun step2(): Long {
        val i = readFile("day12.txt", 2022)
        val nodes =
            i.indices.flatMap { y -> i[y].indices.map { x -> Node(x, y, i[y][x], false, Long.MAX_VALUE) } }.toSet()
        val startingCandidates = nodes.filter { i[it.y][it.x].let { it == 'S' || it == 'a' } }
        val destination = nodes.single { i[it.y][it.x] == 'E' }
        destination.value = 'z' + 1
        val total = startingCandidates.size
        var index = 0
        val distances =  startingCandidates.map {
            ++index
            println("Running with $index of $total")
            var curNode = it
            curNode.distance = 0
            while (curNode != destination) {
                val neighbours = listAccessibleNeighbours(curNode, nodes)
                neighbours.forEach { neighbour ->
                    getNode(neighbour.first, neighbour.second, nodes).let {
                        it.distance = min(it.distance, curNode.distance + 1)
                    }
                }
                curNode.visited = true
                curNode = nodes.filter { !it.visited }.minBy { it.distance }
            }
            val result = destination.distance
            nodes.forEach {
                it.visited = false
                it.distance = Long.MAX_VALUE
            }
            result
        }
        return distances.filter { it > 0 }.min()
    }

    private fun printGrid(nodes: Set<Node>) {
        val maxX = nodes.maxOf { it.x }
        val maxY = nodes.maxOf { it.y }
        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                print(getNode(x, y, nodes).distance.toString().padStart(2, '0') + " ")
            }
            println()
        }
    }

    private fun listAccessibleNeighbours(curNode: Node, nodes: Set<Node>) =
        curNode.let {
            listOf(it.x + 1 to it.y, it.x - 1 to it.y, it.x to it.y + 1, it.x to it.y - 1)
        }
            .asSequence()
            .filter { it.first >= 0 }
            .filter { it.second >= 0 }
            .filter { it.first != nodes.maxOf { it.x } + 1 }
            .filter { it.second != nodes.maxOf { it.y } + 1 }
            .filter { candidate ->
                if (curNode.value == 'S') true
                else
                    getNode(candidate.first, candidate.second, nodes).value.let {
                        curNode.value >= it || curNode.value == it - 1
                    }
            }
            .filter { !getNode(it.first, it.second, nodes).visited }
            .toList()


    private fun getNode(x: Int, y: Int, nodes: Set<Node>): Node = nodes.single { it.x == x && it.y == y }

}


data class Node(
    val x: Int,
    val y: Int,
    var value: Char,
    var visited: Boolean,
    var distance: Long
)
