package twentytwentythree.day4

import readFile
import kotlin.math.pow

class Day4 {
    fun run() {
        val input = readFile("2023-day4.txt")
        val draws = parse(input)
        draws.map { iterateOnCard(it, draws) }.sumOf { it.count() }
                .let { println(it + draws.size) }
    }

    private fun parse(input: List<String>) =
            input.map { it.split(':') }
                    .map { it.zipWithNext() }
                    .map { it.first() }
                    .map { it.first.substring(5).trim().toInt() to it.second }
                    .map {
                        it.first to
                                it.second.split("|")
                                        .map { parseNumberList(it) }
                                        .zipWithNext()
                                        .first()
                    }
                    .map { Card(it.first, it.second.first, it.second.second) }

    private fun parseNumberList(str: String) =
            str.trim()
                    .split(' ')
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .map { it.toInt() }

    private fun computeScore(card: Card): Int =
            card.winningNumbersCount().let { noOfWinningNumbersPlayerHas ->
                if (0 == noOfWinningNumbersPlayerHas) 0 else 2.0.pow(noOfWinningNumbersPlayerHas - 1)
            }.toInt()

    private fun wonCardsForIndividualCard(card: Card, allCards: List<Card>) =
            card.copiesIndicesWon().map { findIndividualCard(it, allCards) }

    private fun findIndividualCard(number: Int, cards: List<Card>) =
            cards.find { it.cardNumber == number }!!

    private fun iterateOnCard(card: Card, reference: List<Card>): List<Card> =
            wonCardsForIndividualCard(card, reference).let { won ->
                if (won.isEmpty()) {
                    won
                } else {
                    won.map { iterateOnCard(it, reference) }.flatten() + won
                }
            }
}

data class Card(
        val cardNumber: Int,
        val winningNumbers: List<Int>,
        val yourNumbers: List<Int>
) {
    fun winningNumbersCount() =
            yourNumbers.count { winningNumbers.contains(it) }

    fun copiesIndicesWon() =
            generateSequence(cardNumber + 1) { it + 1 }.take(winningNumbersCount()).toList()
}
