package twentytwentytwo.day11

import readFile

class Day11 {
    fun run() {
        println(step1())
        println(step2())
    }

    private fun parseInput(file: String) =
        readFile(file, 2022)
            .chunked(7)
            .map {
                Monkey(
                    """Monkey (\d+):""".toRegex().find(it[0])!!.groupValues[1].toInt(),
                    it[1].substring(18).replace(" ", "").split(',').map { it.toLong() }.toMutableList(),
                    """  Operation: new = (old|\d+) ([*+]) (old|\d+)""".toRegex().find(it[2])!!.groupValues.drop(1)
                        .let { Triple(it[0], it[1][0], it[2]) },
                    Triple(
                        """  Test: divisible by (\d+)""".toRegex().find(it[3])!!.groupValues.drop(1).single().toLong(),
                        """    If true: throw to monkey (\d+)""".toRegex().find(it[4])!!.groupValues.drop(1).single()
                            .toInt(),
                        """    If false: throw to monkey (\d+)""".toRegex().find(it[5])!!.groupValues.drop(1).single()
                            .toInt()
                    ), 0
                )
            }

    private fun step1(): Long {
        val monkeys = parseInput("day11.txt")
        repeat(20) {
            monkeys.indices.forEach { handleMonkey(it, monkeys, 3) { i, j: Long -> i / j } }
        }
        return monkeys.map { it.manipulated }.sorted().reversed().take(2).zipWithNext().single().let { it.first * it.second }
    }

    private fun step2(): Long {
        val monkeys = parseInput("day11.txt")
        val ppcm = lcm(monkeys.map { it.movement.first })
        repeat(10000) {
            monkeys.indices.forEach { handleMonkey(it, monkeys, ppcm) { i, j: Long -> i % j }  }
        }
        return monkeys.map { it.manipulated }.sorted().reversed().take(2).zipWithNext().single().let { it.first * it.second }
    }


    private fun handleMonkey(i: Int, monkeys: List<Monkey>, mitigator: Long, mitigatingOperation: (Long, Long) -> Long) {
        val monkey = monkeys.first { i == it.number }
        while (monkey.items.isNotEmpty()) {
            val item = monkey.items.removeFirst()
            ++monkey.manipulated
            val newWorry = mitigateWorry(performOperation(item, monkey.operation), mitigator, mitigatingOperation)
            if (newWorry % monkey.movement.first == 0L)
                monkeys.first { it.number == monkey.movement.second }.items.add(newWorry)
            else
                monkeys.first { it.number == monkey.movement.third }.items.add(newWorry)
        }
    }

    private fun mitigateWorry(worry: Long, mitigator: Long, mitigatingOperation: (Long, Long) -> Long): Long =
        mitigatingOperation.invoke(worry, mitigator)

    private fun performOperation(item: Long, operation: Triple<String, Char, String>): Long {
        val i1 = if (operation.first == "old") item else operation.first.toLong()
        val i2 = if (operation.third == "old") item else operation.third.toLong()
        return if (operation.second == '+') i1 + i2 else i1 * i2
    }

    private fun lcm(items: List<Long>) =
        generateSequence(1L, Long::inc).dropWhile { i -> items.any { i % it != 0L } }.first()
}

data class Monkey(
    val number: Int,
    val items: MutableList<Long>,
    val operation: Triple<String, Char, String>,
    val movement: Triple<Long, Int, Int>,
    var manipulated: Long
)
