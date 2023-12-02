package twentytwentythree.day2

import readFile

class Day2 {
    fun run() {
        val input = readFile("2023-day2.txt")
        val games = parse(input)
        println(games.filter { testIfGamePossible(it) }.sumOf { it.id })
        games.map { findMaxNumberOfAllColoursForGame(it) }.sumOf { it.reduce { acc, i -> acc * i } }
                .let { println(it) }
    }

    private fun parse(input: List<String>): List<Game> {
        return input.map { it.split(':') }
                .map { it[0] to it[1].trim() }
                .map { it.first.substring(5).toInt() to it.second }
                .map { it.first to it.second.split(';') }
                .map {
                    it.first to
                            it.second.map { reveal -> reveal.split(',') }
                                    .map { reveal -> reveal.map { it.trim() } }
                                    .map { reveal -> reveal.flatMap { cubedisplay -> cubedisplay.split(' ').zipWithNext() } }
                                    .map { reveal -> reveal.map { cubedisplay -> CubeDisplay(cubedisplay.first.toInt(), cubedisplay.second) } }
                }
                .map { Game(it.first, it.second) }
    }

    private fun testIfGamePossible(game: Game): Boolean =
            findMaxNumberOfColourRevealedInGame(game, "red") <= 12 &&
                    findMaxNumberOfColourRevealedInGame(game, "green") <= 13 &&
                    findMaxNumberOfColourRevealedInGame(game, "blue") <= 14

    private fun findMaxNumberOfColourInReveal(reveal: Reveal, colour: String): Int =
            reveal.filter { colour == it.colour }.maxOfOrNull { it.number } ?: 0

    private fun findMaxNumberOfColourRevealedInGame(game: Game, colour: String): Int =
            game.reveals.maxOfOrNull { findMaxNumberOfColourInReveal(it, colour) } ?: 0

    private fun findMaxNumberOfAllColoursForGame(game: Game): Collection<Int> =
            colours.map { colour -> findMaxNumberOfColourRevealedInGame(game, colour) }
}

val colours = setOf("red", "green", "blue")

data class Game(
        val id: Int,
        val reveals: List<Reveal>
)

typealias Reveal = List<CubeDisplay>

data class CubeDisplay(
        val number: Int,
        val colour: String
)
