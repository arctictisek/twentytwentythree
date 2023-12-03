package twentytwentythree.day3

import readFile
import kotlin.math.abs

class Day3 {
    fun run() {
        val input = readFile("2023-day3.txt")
        part2(input)
    }

    private fun part1(input: List<String>) {
        val nonEmptySquares = parseInputToUnprocessedSquares(input)
                .filter { it.isDigit || it.isSymbol }
        // println(nonEmptySquares)
        val digits = input.indices.flatMap { y ->
            findDigits(nonEmptySquares.filter { it.coordinates.second == y }, y)
        }
        val symbols = input.indices.flatMap { y ->
            findSymbols(nonEmptySquares.filter { it.coordinates.second == y }, y)
        }
        colourDigits(digits, symbols)
        propagateAllColours(digits)

        val allColouredDigits =
                digits.filter { it.adjacent ?: false }.groupBy { it.coordinates.second }.map { it.value }
        val allStrings = allColouredDigits.map { buildString(it) }.flatten().sum()
        println(allStrings)
    }

    private fun parseInputToUnprocessedSquares(input: List<String>): List<Square> =
            input.flatMapIndexed { index, line ->
                parseLineToUnprocessedSquares(line, index)
            }

    private fun parseLineToUnprocessedSquares(input: String, y: Int): List<Square> =
            input.mapIndexed { index, c ->
                Square(c, null, c.isDigit(), !c.isDigit() && c != '.', index to y, null)
            }

    private fun findDigits(squares: List<Square>, y: Int): List<Square> =
            squares.filter { y == it.coordinates.second }
                    .filter { it.isDigit }

    private fun findSymbols(squares: List<Square>, y: Int): List<Square> =
            squares.filter { y == it.coordinates.second }
                    .filter { it.isSymbol }


    private fun colourDigits(digits: List<Square>, symbols: List<Square>) {
        digits.forEach { digit ->
            symbols.forEach { symbol ->
                if (areAdjacent(digit, symbol)) {
                    digit.adjacent = true
                }
            }
        }
    }

    private fun propagateColour(partiallyColouredDigits: List<Square>, y: Int) {
        val greenSquares =
                partiallyColouredDigits.filter { it.adjacent == true && it.coordinates.second == y }
        for (square in greenSquares) {
            var x = square.coordinates.first
            while (true) {
                val newSquare = getSquare(partiallyColouredDigits, x to y)
                if (newSquare.isDigit) {
                    newSquare.adjacent = true
                    --x
                } else {
                    break
                }
            }
            x = square.coordinates.first
            while (true) {
                val newSquare = getSquare(partiallyColouredDigits, x to y)
                if (newSquare.isDigit) {
                    newSquare.adjacent = true
                    ++x
                } else {
                    break
                }
            }
        }
    }

    private fun propagateAllColours(partiallyColouredDigits: List<Square>) {
        val maxY = partiallyColouredDigits.map { it.coordinates }.maxOf { it.second }
        (0..maxY).forEach { y -> propagateColour(partiallyColouredDigits, y) }
    }

    private fun areAdjacent(digit: Square, symbol: Square): Boolean =
            1 >= abs(digit.coordinates.first - symbol.coordinates.first) &&
                    1 >= abs(digit.coordinates.second - symbol.coordinates.second)

    private fun getSquare(squares: List<Square>, xy: Pair<Int, Int>): Square =
            squares.find { it.coordinates == xy } ?: Square('!', false, false, false, 0 to 0, null)

    private fun buildString(squaresOfLine: List<Square>): List<Int> {
        val tempResult = MutableList(squaresOfLine.maxOf { it.coordinates.first + 1 }) { ' ' }
        squaresOfLine.forEach { tempResult[it.coordinates.first] = it.char }
        return tempResult.joinToString("").trim().split(' ').filter { it.trim().isNotBlank() }
                .map { it.toInt() }
    }

    private fun buildSquare(xy: Pair<Int, Int>): Square =
            Square('.', null, true, true, xy, null)

    private fun part2(input: List<String>) {
        val improvedInput = input.map { line ->
            line.map { char ->
                if (char.isDigit()) {
                    char
                } else if ('*' == char) {
                    '*'
                } else {
                    '.'
                }
            }.joinToString("")
        }
//        improvedInput.forEach { println(it) }


        val nonEmptySquares = parseInputToUnprocessedSquares(improvedInput)
                .filter { it.isDigit || it.isSymbol }
        val digits = improvedInput.indices.flatMap { y ->
            findDigits(nonEmptySquares.filter { it.coordinates.second == y }, y)
        }
        val symbols = improvedInput.indices.flatMap { y ->
            findSymbols(nonEmptySquares.filter { it.coordinates.second == y }, y)
        }
        colourDigits(digits, symbols)
        propagateAllColours(digits)

        val processedSquares =
                (digits + symbols).filter { it.isSymbol || (it.adjacent ?: false) }.groupBy { it.coordinates.second }
                        .toSortedMap()
        val result = (0 until input.size).map {
            buildRawString(processedSquares.getOrDefault(it, listOf()), input[0].length)
        }
        result.forEach { println(it) }

        val newUnprocessed = parseInputToUnprocessedSquares(result)
        val newSymbols = result.indices.flatMap { y ->
            findSymbols(nonEmptySquares.filter { it.coordinates.second == y }, y)
        }
        val surroundingDigits = newSymbols.map { findSurroundingDigits(it, newUnprocessed) }
        surroundingDigits.map { symbol -> symbol.map { reconstructNumber(it, newUnprocessed) }.distinct() }
                .filter { it.size == 2 }
                .map { setOf(it[0], it[1]) }
                .distinct()
                .map { it.toList() }
                .onEach { println(it) }
                .map { it[0] * it[1] }
                .sum()
                .let { println(it) }
    }

    private fun buildRawString(squaresOfLine: List<Square>, length: Int): String {
        val tempResult = MutableList(length) { '.' }
        squaresOfLine.forEach { tempResult[it.coordinates.first] = it.char }
        return tempResult.joinToString("")
    }

    private fun findSurroundingDigits(symbol: Square, allSquares: List<Square>): List<Square> {
        val coordinates = symbol.coordinates
        return listOf(
                getSquare(allSquares, coordinates.first - 1 to coordinates.second - 1),
                getSquare(allSquares, coordinates.first - 1 to coordinates.second),
                getSquare(allSquares, coordinates.first - 1 to coordinates.second + 1),

                getSquare(allSquares, coordinates.first to coordinates.second - 1),
                getSquare(allSquares, coordinates.first to coordinates.second + 1),

                getSquare(allSquares, coordinates.first + 1 to coordinates.second - 1),
                getSquare(allSquares, coordinates.first + 1 to coordinates.second),
                getSquare(allSquares, coordinates.first + 1 to coordinates.second + 1)
        ).filter { it.isDigit }
    }

    private fun reconstructNumber(digit: Square, allSquares: List<Square>): Int {
        val lineDigit = allSquares.filter { it.isDigit }
                .filter { it.coordinates.second == digit.coordinates.second }
        val digitsSurroundingDigits = findDigitSurroundingDigit(digit, lineDigit)
        return digitsSurroundingDigits.map { it.char }.joinToString("").toInt()
    }

    private fun findDigitSurroundingDigit(digit: Square, lineDigit: List<Square>): List<Square> {
        val x = digit.coordinates.first
        val lowerDigits = (x downTo 0).map { getSquare(lineDigit, it to digit.coordinates.second) }.takeWhile { it.isDigit }
        val higherDigits = (x until  1000).map { getSquare(lineDigit, it to digit.coordinates.second) }.takeWhile { it.isDigit }
        return (lowerDigits + higherDigits).distinct().sortedBy { it.coordinates.first }
    }
}

data class Square(
        val char: Char,
        var adjacent: Boolean?,
        val isDigit: Boolean,
        val isSymbol: Boolean,
        val coordinates: Pair<Int, Int>,
        var doubleSurrounded: Boolean?
)

