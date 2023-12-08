package twentytwentythree.day8

import readFile

class Day8 {
    fun run() {
        val input = readFile("2023-day8.txt")
        val network = parse(input)
        println(followPath(network))
    }

    private fun parse(input: List<String>): Network =
            Network(input[0], input.drop(2).associate {
                it.substring(0, 3) to Choice(
                        it.substring(7, 10),
                        it.substring(12, 15))
            })

    //    private fun followPath(curNode: String, curStep: Int, curIndex: Int, network: Network): Int {
//        return if (curNode == "ZZZ") {
//            curStep
//        } else {
//            println(curStep)
//            followPath(
//                    network.nodes[curNode]!!.choose(network.instructions.modGet(curIndex)),
//                    curStep + 1,
//                    curIndex + 1,
//                    network
//            )
//        }
//    }
    private fun followPath(network: Network): Int {
        var curNode = "AAA"
        var curStep = 0
        var curIndex = 0
        while (curNode != "ZZZ") {
            curNode = network.nodes[curNode]!!.choose(network.instructions.modGet(curIndex))
            ++curStep
            ++curIndex
        }
        return curIndex
    }
}


data class Network(
        val instructions: String,
        val nodes: Map<String, Choice>
)

data class Choice(
        val left: String,
        val right: String
) {
    fun choose(c: Char) = if ('L' == c) left else right
}

fun String.modGet(n: Int): Char = this[(n % this.length)]
