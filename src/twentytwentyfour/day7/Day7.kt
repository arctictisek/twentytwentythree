package twentytwentyfour.day7

import p
import readFile

class Day7 {
    fun run() {
        val input = readFile("day7.txt", 2024)
        val parsed = parseInput(input)
        parsed.filter { producesTheTestValuePart2(it) }.sumOf { it.result }.p()
    }

    fun parseLine(line: String) =
        line.split(":").zipWithNext().first().let { (result, values) ->
            Equation(
                result.toLong(),
                values.split(" ").filter { it.isNotBlank() }.map { it.toLong() }
            )
        }

    fun parseInput(input: List<String>) = input.map { parseLine(it) }

    fun reduce(values: List<Long>): List<Long> {
        if (1 == values.size) return values
        else {
            val (first, second) = values.take(2)
            val rest = values.drop(2)
            return reduce(listOf(first + second) + rest) + reduce(listOf(first * second) + rest)
        }
    }

    fun reducePart2(values: List<Long>): List<Long> {
        if (1 == values.size) return values
        else {
            val (first, second) = values.take(2)
            val rest = values.drop(2)
            return reducePart2(listOf(first + second) + rest) +
                    reducePart2(listOf(first * second) + rest) +
                    reducePart2(listOf((first.toString() + second.toString()).toLong()) + rest)
        }
    }

    fun producesTheTestValue(equation: Equation) = reduce(equation.values).any { it == equation.result }
    fun producesTheTestValuePart2(equation: Equation) = reducePart2(equation.values).any { it == equation.result }
}

data class Equation(
    val result: Long,
    val values: List<Long>
)
