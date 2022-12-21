package twentytwentytwo.day20

import readFile

class Day20 {
    fun run() {
        println(step1())
        println(step2())
    }

    private fun step1(): Int {
        val input = readFile("day20.txt").map { it.toInt() }
        val toWork = input.toMutableList()
        input.forEach {
            if (it != 0) {
                val indexOfIt = toWork.indexOf(it)
                toWork.removeAt(indexOfIt)
                var newIndex = indexOfIt + it
                if (newIndex <= 0) {
                    while (newIndex <= 0) {
                        newIndex += toWork.size
                    }
                } else if (newIndex > toWork.size) {
                    newIndex %= toWork.size
                }
                toWork.add(newIndex, it)
            }
        }
        val zeroLocation = toWork.indexOf(0)
        println("1000 after 0: " + toWork[(zeroLocation + 1000) % toWork.size])
        println("2000 after 0: " + toWork[(zeroLocation + 2000) % toWork.size])
        println("3000 after 0: " + toWork[(zeroLocation + 3000) % toWork.size])
        return toWork[(zeroLocation + 1000) % toWork.size] + toWork[(zeroLocation + 2000) % toWork.size] + toWork[(zeroLocation + 3000) % toWork.size]
    }





    private fun step2() = 0
}
