package twentytwentythree.day1

import readFile

class Day1 {
    fun run() {
        val input = readFile("2023-day1Simple.txt")
        println(sumFirstAndLast(input))
        println(transformInput(input))
    }

    private fun sumFirstAndLast(input: List<String>) =
            input
                    .map { it.filter { it.isDigit() } }
                    .sumOf { it.first().digitToInt() * 10 + it.last().digitToInt() }

    private fun transformInput(input: List<String>) =
            input.map { findExtremeNumbers(it) }
                    .sumOf { it.first * 10 + it.second}

    private fun findExtremeNumbers(input: String): Pair<Int, Int> {
        return findSpecificNumber(input, { inputStr: String, search: String, -> inputStr.indexOf(search) }, 1) to
                findSpecificNumber(input, { inputStr: String, search: String, -> inputStr.lastIndexOf(search)}, -1)
    }

    private fun findSpecificNumber(input: String, function: (String, String) -> (Int), filterMult: Int): Int {
        return listOf(
                "1" to function(input, "1"),
                "2" to function(input, "2"),
                "3" to function(input, "3"),
                "4" to function(input, "4"),
                "5" to function(input, "5"),
                "6" to function(input, "6"),
                "7" to function(input, "7"),
                "8" to function(input, "8"),
                "9" to function(input, "9"),
                "1" to function(input, "one"),
                "2" to function(input, "two"),
                "3" to function(input, "three"),
                "4" to function(input, "four"),
                "5" to function(input, "five"),
                "6" to function(input, "six"),
                "7" to function(input, "seven"),
                "8" to function(input, "eight"),
                "9" to function(input, "nine"),
        )
                .sortedBy { it.second * filterMult }.first { -1 != it.second }
                .let { doTransform(it.first) }
    }

    private fun doTransform(input: String): Int = when (input) {
        "one" -> "1"
        "two" -> "2"
        "three" -> "3"
        "four" -> "4"
        "five" -> "5"
        "six" -> "6"
        "seven" -> "7"
        "eight" -> "8"
        "nine" -> "9"
        else -> input
    }[0].digitToInt()

}
