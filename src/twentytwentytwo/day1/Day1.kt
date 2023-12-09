package twentytwentytwo.day1

import readFile

class Day1 {
    fun run() {
        println(maxFile())
        println(topThree())
    }

    private fun maxFile(): Int {
        var max = 0
        var sum = 0
        for (line in readFile("day1.txt", 2022)) {
            if (line.isNotBlank()) {
                sum += line.toInt()
            } else {
                if (sum > max) {
                    max = sum
                }
                sum = 0
            }
        }
        return max
    }

    private fun topThree(): Int {
        val list = mutableListOf<Int>()
        var sum = 0
        for (line in readFile("day1.txt", 2022)) {
            if (line.isNotBlank()) {
                sum += line.toInt()
            } else {
                list.add(sum)
                sum = 0
            }
        }
        return list.sortedDescending().take(3).sum()
    }
}
