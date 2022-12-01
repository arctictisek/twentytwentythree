package twentytwentytwo.day2

import twentytwentytwo.day2.PlayResult.Companion.neededOutcome

enum class RockPaperScissors(private val opponentCode: Char, private val playerCode: Char, private val playScore: Int) {
    ROCK('A', 'X', 1),
    PAPER('B', 'Y', 2),
    SCISSORS('C', 'Z', 3);

    fun findWinningPlay(): RockPaperScissors =
        when (this) {
            ROCK -> PAPER
            PAPER -> SCISSORS
            SCISSORS -> ROCK
        }

    fun findLosingPlay(): RockPaperScissors =
        when (this) {
            ROCK -> SCISSORS
            PAPER -> ROCK
            SCISSORS -> PAPER
        }

    companion object {
        fun retrieveOpponentMove(c: Char): RockPaperScissors = values().find { c == it.opponentCode }!!

        fun retrievePlayerMove(c: Char): RockPaperScissors = values().find { c == it.playerCode }!!

        private fun whoWins(playerPlay: RockPaperScissors, opponentPlay: RockPaperScissors): PlayResult =
            when (playerPlay) {
                opponentPlay -> PlayResult.DRAW
                ROCK -> if (SCISSORS == opponentPlay) PlayResult.PLAYER_WIN else PlayResult.ELF_WINS
                PAPER -> if (ROCK == opponentPlay) PlayResult.PLAYER_WIN else PlayResult.ELF_WINS
                SCISSORS -> if (PAPER == opponentPlay) PlayResult.PLAYER_WIN else PlayResult.ELF_WINS
            }

        fun score(playerPlay: RockPaperScissors, opponentPlay: RockPaperScissors): Int =
            playerPlay.playScore + whoWins(playerPlay, opponentPlay).score

        private fun selectPlay(opponentPlay: RockPaperScissors, playerNeed: RockPaperScissors): RockPaperScissors =
            when (neededOutcome(playerNeed.playerCode)) {
                PlayResult.DRAW -> opponentPlay
                PlayResult.PLAYER_WIN -> opponentPlay.findWinningPlay()
                PlayResult.ELF_WINS -> opponentPlay.findLosingPlay()
            }

        fun scoreStep2(opponentPlay: RockPaperScissors, playerPlay: RockPaperScissors): Int =
            neededOutcome(playerPlay.playerCode).score + selectPlay(opponentPlay, playerPlay).playScore
    }
}

enum class PlayResult(@JvmField val score: Int, private val neededOutcome: Char) {
    PLAYER_WIN(6, 'Z'),
    ELF_WINS(0, 'X'),
    DRAW(3, 'Y');

    companion object {
        @JvmStatic
        fun neededOutcome(c: Char): PlayResult = values().find { c == it.neededOutcome }!!
    }
}
