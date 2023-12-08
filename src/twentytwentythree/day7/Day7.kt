package twentytwentythree.day7

import readFile

class Day7 {
    fun run() {
        val input = readFile("2023-day7.txt")
        val games = parse(input)
        games.sorted().also { it.forEach { println(it.hand.toList().joinToString("")) } }.mapIndexed { index, game -> game.bid * (index + 1) }.sum().let { println(it) }
    }

    private fun parse(input: List<String>) =
            input.map { it.split(" ") }
                    .map { it[0] to it[1] }
                    .map { it.first to it.second.toInt() }
                    .map { Game(it.first, it.second) }


}

private fun Char.toCardInt(): Int {
    return if (this in setOf('J', 'Q', 'K', 'A')) {
        when (this) {
            'J' -> 1
            'Q' -> 12
            'K' -> 13
            'A' -> 14
            else -> -1
        }
    } else if (this == 'T') 10
    else
        this.digitToInt()

}

data class Game(
        val hand: String,
        val bid: Int
) : Comparable<Game> {
    override fun compareTo(other: Game) =
            rankHandsWithJokers().compareTo(other.rankHandsWithJokers()).let {
                if (it != 0) it else tieBreak(other)
            }

    private fun isFiveOfAKind() = 1 == hand.toSet().size
    private fun isQuads() = hand.toSet().any { uniqueCard -> hand.count { it == uniqueCard } == 4 }
    private fun isBoat() = hand.toSet().let { setOfUniqueCards ->
        setOfUniqueCards.size == 2 &&
                setOfUniqueCards.map { uniqueCard -> hand.count { it == uniqueCard } }
                        .sorted() == listOf(2, 3)
    }

    private fun isTwoPair() = hand.toSet().let { setOfUniqueCards ->
        setOfUniqueCards.size == 3 &&
                setOfUniqueCards.map { uniqueCard -> hand.count { it == uniqueCard } }
                        .sorted() == listOf(1, 2, 2)
    }

    private fun isTrips() = hand.toSet().let { setOfUniqueCards ->
        setOfUniqueCards.size == 3 &&
                setOfUniqueCards.map { uniqueCard -> hand.count { it == uniqueCard } }
                        .sorted() == listOf(1, 1, 3)
    }

    private fun isOnePair() = hand.toSet().let { setOfUniqueCards ->
        setOfUniqueCards.size == 4 &&
                setOfUniqueCards.map { uniqueCard -> hand.count { it == uniqueCard } }
                        .sorted() == listOf(1, 1, 1, 2)
    }

    private fun isHighCard(hand: String) = 5 == hand.toSet().size

    private fun rankHand(): Rank {
        return when {
            isFiveOfAKind() -> Rank.FIVE
            isQuads() -> Rank.FOUR
            isTrips() -> Rank.THREE
            isTwoPair() -> Rank.TWO
            isBoat() -> Rank.BOAT
            isOnePair() -> Rank.ONE
            else -> Rank.HIGH
        }
    }

    private fun rankHandsWithJokers(): Rank {
        return setOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
                .map { hand.replace('J', it) }
                .map { Game(it, this.bid) }
                .maxOf { it.rankHand() }
    }

    private fun tieBreak(other: Game): Int {
        val cardInts = this.hand.map { it.toCardInt() }
        val otherCardInts = other.hand.map { it.toCardInt() }
        (0 until 5).forEach { if (cardInts[it] != otherCardInts[it]) return cardInts[it].compareTo(otherCardInts[it]) }
        return 0
    }

}

enum class Rank(value: Int): Comparable<Rank> {
    HIGH(4),
    ONE(5),
    TWO(6),
    THREE(7),
    BOAT(8),
    FOUR(9),
    FIVE(10)
}
