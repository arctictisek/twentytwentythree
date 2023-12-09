package twentytwentythree.day6

import readFile

class Day6 {
    fun run() {
        val input = readFile("day6.txt", 2023)
        parse(input).map { it.waysToBeatRecords() }
                .reduce { acc, i -> acc * i }
                .also { println(it) }
        parsePart2(input).waysToBeatRecords().also { println(it) }
    }

    private fun parse(input: List<String>) =
            input.asSequence()
                    .map { it.replace("Time:", "") }
                    .map { it.replace("Distance:", "") }
                    .map { it.trim() }
                    .map { it.split(' ') }
                    .map { it.filter { it.isNotBlank() } }
                    .map { it.map { it.toLong() } }.toList()
                    .let { lists ->
                        (0 until lists[0].size).map { Race(lists[0][it], lists[1][it]) }
                    }

    private fun parsePart2(input: List<String>) =
            input.asSequence()
                    .map { it.replace("Time:", "") }
                    .map { it.replace("Distance:", "") }
                    .map { it.replace(" ", "") }
                    .map { it.trim() }
                    .map { it.toLong() }
                    .zipWithNext()
                    .map { Race(it.first, it.second) }
                    .first()

}

data class Race(
        val time: Long,
        val distance: Long
) {
    private fun computeDistancePerTimePressed(pressed: Long) = pressed * (time - pressed)
    private fun timePressedBeatsRecord(pressed: Long) = computeDistancePerTimePressed(pressed) > distance
    fun waysToBeatRecords() = (0..time).count { pressed -> timePressedBeatsRecord(pressed) }
}
