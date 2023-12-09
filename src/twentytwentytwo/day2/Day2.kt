package twentytwentytwo.day2

import readFile

class Day2 {
    fun run() {
        println(computeScore())
        println(stepTwo())
    }

    private fun computeScore(): Int =
        readFile("day2.txt", 2022).sumOf {
            RockPaperScissors.score(
                RockPaperScissors.retrievePlayerMove(it[2]),
                RockPaperScissors.retrieveOpponentMove(it[0])
            )
        }

    private fun stepTwo(): Int =
        readFile("day2.txt", 2022).sumOf {
            RockPaperScissors.scoreStep2(
                RockPaperScissors.retrieveOpponentMove(it[0]),
                RockPaperScissors.retrievePlayerMove(it[2])
            )
        }
}
