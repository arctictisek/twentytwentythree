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
            'J' -> 11
            'Q' -> 12
            'K' -> 13
            'A' -> 1
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
            rankHand().compareTo(other.rankHand()).let {
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

    private fun tieBreak(other: Game): Int {
        if (rankHand() == Rank.FIVE) {
            return this.hand[0].toCardInt().compareTo(other.hand[0].toCardInt())
        } else if (rankHand() == Rank.FOUR) {
            val quadCard = this.hand.toSet().find { uniqueCard -> this.hand.count { uniqueCard == it } == 4 }!!
            val otherQuadCard = other.hand.toSet().find { uniqueCard -> other.hand.count { uniqueCard == it } == 4 }!!
            return if (quadCard != otherQuadCard) quadCard.toCardInt().compareTo(otherQuadCard.toCardInt())
            else this.hand.replace("" + quadCard, "")[0].toCardInt()
                    .compareTo(other.hand.replace("" + quadCard, "")[0].toCardInt())
        } else if (rankHand() == Rank.THREE) {
            val tripCard = this.hand.toSet().find { uniqueCard -> this.hand.count { uniqueCard == it } == 3 }!!
            val otherTripCard = other.hand.toSet().find { uniqueCard -> other.hand.count { uniqueCard == it } == 3 }!!
            return if (tripCard != otherTripCard) tripCard.toCardInt().compareTo(otherTripCard.toCardInt())
            else {
                val kickers = this.hand.replace("" + tripCard, "").map { it.toCardInt() }.toSortedSet().toList().reversed()
                val otherKickers = other.hand.replace("" + tripCard, "").map { it.toCardInt() }.toSortedSet().toList().reversed()
                if (kickers[0] == otherKickers[0]) kickers[1].compareTo(otherKickers[1]) else kickers[0].compareTo(otherKickers[0])
            }
        } else if (rankHand() == Rank.BOAT) {
            val tripCard = this.hand.toSet().find { uniqueCard -> this.hand.count { uniqueCard == it } == 3 }!!.toCardInt()
            val otherTripCard = other.hand.toSet().find { uniqueCard -> other.hand.count { uniqueCard == it } == 3 }!!.toCardInt()
            val pairCard = this.hand.toSet().find { uniqueCard -> this.hand.count { uniqueCard == it } == 2 }!!.toCardInt()
            val otherPairCard = other.hand.toSet().find { uniqueCard -> other.hand.count { uniqueCard == it } == 2 }!!.toCardInt()
            if (tripCard != otherTripCard) tripCard.compareTo(otherTripCard)
            else pairCard.compareTo(otherPairCard)
        } else if (rankHand() == Rank.TWO) {
            val pairCards = this.hand.toSortedSet()
                    .filter { uniqueCard -> this.hand.count { uniqueCard == it } == 2 }
                    .map { it.toCardInt() }
                    .sorted()
                    .reversed()
            val otherPairCards = other.hand.toSortedSet()
                    .filter { uniqueCard -> other.hand.count { uniqueCard == it } == 2 }
                    .map { it.toCardInt() }
                    .sorted()
                    .reversed()
            return if (pairCards[0] != otherPairCards[0]) pairCards[0].compareTo(otherPairCards[0])
            else if (pairCards[1] != otherPairCards[1]) pairCards[1].compareTo(otherPairCards[1])
            else {
                println(this.hand + " " + other.hand)
                val kicker = this.hand.map { it.toCardInt() }.filter { !pairCards.contains(it) }[0]
                val otherKicker = other.hand.map { it.toCardInt() }.filter { !otherPairCards.contains(it) }[0]
                kicker.compareTo(otherKicker)
            }
        } else if (rankHand() == Rank.ONE) {
            val pairCard = this.hand.toSortedSet()
                    .filter { uniqueCard -> this.hand.count { uniqueCard == it } == 2 }
                    .map { it.toCardInt() }
                    .first()
            val otherPairCard = other.hand.toSortedSet()
                    .filter { uniqueCard -> other.hand.count { uniqueCard == it } == 2 }
                    .map { it.toCardInt() }
                    .first()
            if (pairCard != otherPairCard) return pairCard.compareTo(otherPairCard)
            else {
                val kickers = this.hand.map { it.toCardInt() }.filter { it != pairCard }.sortedDescending()
                val otherKickers = other.hand.map { it.toCardInt() }.filter { it != otherPairCard }.sortedDescending()
                (0 until 3).forEach { if (kickers[it] != otherKickers[it]) return kickers[it].compareTo(otherKickers[it]) }
                return 0
            }
        } else if (rankHand() == Rank.HIGH) {
            val kickers = this.hand.map { it.toCardInt() }.sortedDescending()
            val otherKickers = other.hand.map { it.toCardInt() }.sortedDescending()
            (0 until 5).forEach { if (kickers[it] != otherKickers[it]) return kickers[it].compareTo(otherKickers[it]) }
            return 0
        }
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
