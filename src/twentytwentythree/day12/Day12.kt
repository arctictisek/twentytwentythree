package twentytwentythree.day12

import readFile

class Day12 {
    fun run() {
        val input = readFile("day12.txt", 2023)
        val rows = parse(input)
        rows.sumOf { it.countPossibleArrangements() }.let { println(it) }
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


    private fun String.numberOfDamagedSprings() =
            this
                    .onEach { if (!setOf('?', '#', '.').contains(it)) throw IllegalArgumentException() }
                    .count { it == '#' }


}


data class Row(
        val springs: String,
        val damagedLayout: List<Int>
) {
    fun countPossibleArrangements() =
            springs.generateCombinations()
                    .count { it.clearRowToLayout() == damagedLayout }

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

    private fun String.clearRowToLayout() =
            this.split('.')
                    .filter { it != "" }
                    .onEach { if (it.contains('?')) throw IllegalArgumentException() }
                    .map { it.length }
}
