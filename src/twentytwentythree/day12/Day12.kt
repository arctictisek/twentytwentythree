package twentytwentythree.day12

import readFile

class Day12 {
    fun run() {
        val input = readFile("day12Simple.txt", 2023)
        val rows = parse(input)
//        rows.sumOf { it.countPossibleArrangements() }.let { println(it) }
        val newRows = rows.map { it.expandFiveTimes() }
//        newRows.forEach { println(it) }
        newRows.sumOf { it.countPossibleArrangements() }.let { println(it) }
    }

    private fun parse(input: List<String>): List<Row> =
            input.asSequence()
                    .map { it.split(' ') }
                    .map { it.first() to it.last() }
                    .map { (springs, layout) ->
                        springs to layout.split(',')
                    }
                    .map { (springs, layout) ->
                        springs to layout.map { it.toInt() }
                    }
                    .map { (springs, layout) ->
                        Row(springs, layout)
                    }.toList()

}


data class Row(
        val springs: String,
        val damagedLayout: List<Int>
) {
    fun countPossibleArrangements() =
            springs.generateCombinations()
                    .count { it.clearRowToLayout() == damagedLayout }
                    .also { println("$springs -> $it") }

    private fun String.generateCombinations(): List<String> {
        val result = mutableListOf<String>()

        fun generateCombinationsHelper(template: String, current: String) {
            if (template.isEmpty()) {
                result.add(current)
                return
            }

            val firstChar = template[0]
            val restOfTemplate = template.substring(1)

            if (firstChar == '?') {
                generateCombinationsHelper(restOfTemplate, "$current#")
                generateCombinationsHelper(restOfTemplate, "$current.")
            } else {
                generateCombinationsHelper(restOfTemplate, current + firstChar)
            }
        }

        generateCombinationsHelper(this, "")
        return result
    }

    fun expandFiveTimes() =
            Row(
                    generateSequence { springs }.take(5)
                            .joinToString("?"),
                    generateSequence { damagedLayout }.take(5)
                            .flatten()
                            .toList())
}

private fun String.clearRowToLayout() =
        this.split('.')
                .filter { it != "" }
                .onEach { if (it.contains('?')) throw IllegalArgumentException() }
                .map { it.length }

