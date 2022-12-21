package twentytwentytwo.day16

import readFile

class Day16 {
    fun run() {
        println(step1())
        println(step2())
    }

    private fun step1(): Int {
        val graph = readInput("day16Simple.txt")
        val visited = graph.associate { it.first to 0 }.toMutableMap()
        traverseGraph(graph, "AA", visited)
        return 0
    }

    private fun traverseGraph(
        graph: List<Triple<String, String, List<String>>>,
        vertex: String,
        visited: MutableMap<String,Int>
    ) {
        println("Visiting $vertex")
        visited[vertex] = visited.getOrDefault(vertex, 0) + 1
        val children = graph.find { it.first == vertex }!!.let { it.third }
        val unexploredChildren = children.minBy { visited.getOrDefault(it, 0) }
        traverseGraph(graph, unexploredChildren, visited)
    }


    private fun step2(): Int = 0


    private fun readInput(file: String) = readFile(file)
        .map {
            """Valve (..) has flow rate=(\d+); tunnels? leads? to valves? (.*)""".toRegex()
                .find(it)!!
                .groupValues
        }
        .map { it.drop(1) }
        .map { Triple(it[0], it[1], it[2].split(", ").toList()) }

//        .map { """(\d+)\-(\d+),(\d+)\-(\d+)""".toRegex().find(it)!!.groupValues }

}
