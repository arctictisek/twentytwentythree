package twentytwentyfour.day4

import p
import readFile

class Day4 {
    fun run() {
        val input = readFile("day4.txt", 2024)
//        countXmas(input).p()
        extractSquares(input).count { isCrossXmas(it) }.p()
    }

    fun countXmas(input: List<String>): Int {
        val horizontal = input.sumOf { horizontalCount(it, "XMAS") + horizontalCount(it.reversed(), "XMAS") }
        val vertical = flipTextBlock(input).sumOf { horizontalCount(it, "XMAS") + horizontalCount(it.reversed(), "XMAS") }
        val diagonal = countDiagonalOccurrences(input, "XMAS")
        println("H$horizontal V$vertical D$diagonal")
        return horizontal + vertical + diagonal
    }

    fun horizontalCount(str: String, searchStr: String): Int {
        var count = 0
        var startIndex = 0

        while (startIndex < str.length) {
            val index = str.indexOf(searchStr, startIndex)
            if (index >= 0) {
                count++
                startIndex = index + 1
            } else {
                break
            }
        }

        return count
    }

    fun flipTextBlock(text: List<String>): List<String> {
        return if (text.isEmpty()) {
            emptyList()
        } else {
            (0 until text[0].length).map { columnIndex ->
                text.map { row -> row[columnIndex] }
            }.map { it.joinToString("") }
        }
    }

    fun countDiagonalOccurrences(text: List<String>, word: String): Int {
        val directions = listOf(1 to 1, -1 to -1, -1 to 1, 1 to -1)

        fun checkDirection(row: Int, col: Int, dRow: Int, dCol: Int): Boolean {
            return word.indices.all { i ->
                val newRow = row + i * dRow
                val newCol = col + i * dCol
                newRow in text.indices && newCol in text[newRow].indices && text[newRow][newCol] == word[i]
            }
        }

        return text.indices.sumOf { row ->
            text[row].indices.sumOf { col ->
                directions.count { (dRow, dCol) -> checkDirection(row, col, dRow, dCol) }
            }
        }
    }

    fun extractSquares(text: List<String>): List<List<String>> {
        val squares = mutableListOf<List<String>>()

        for (i in 0..text.size - 3) {
            for (j in 0..text[i].length - 3) {
                val square = (0..2).map { row ->
                    text[i + row].substring(j, j + 3)
                }
                squares.add(square)
            }
        }

        return squares
    }

    fun isCrossXmas(input: List<String>): Boolean {
        return 2 == countDiagonalOccurrences(input, "MAS")
    }

}
