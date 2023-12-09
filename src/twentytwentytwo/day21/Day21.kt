package twentytwentytwo.day21

import readFile

class Day21 {
    fun run() {
        println(step1())
        println(step2())
    }

    private fun step1(): Long {
        val input = readFile("day21.txt", 2022)
            .map { """(....): (.*)""".toRegex().find(it)!!.groupValues }
            .map { it.drop(1) }
            .map { it.zipWithNext().single() }
            .partition { it.second.all { it.isDigit() } }
        val values = input.first.associateBy({ it.first }, { ActualShout(it.second.toLong()) })
        val operations =
            input.second.map { it.first to """(....) (.) (....)""".toRegex().find(it.second)!!.groupValues }
                .map { it.first to it.second.drop(1) }
                .map { it.first to Triple(it.second[0], it.second[1][0], it.second[2]) }
                .map { it.first to OperationShout(it.second) }
                .associateBy( { it.first }, { it.second } )
        return eval("root", values, operations)
    }

    private fun eval(toEval: String,
                     values: Map<String, ActualShout>,
                     operations: Map<String, OperationShout>,
                     defaultHumn: Long = 0L): Long =
        if (values.containsKey(toEval) || toEval == "humn") {
            values.getOrDefault(toEval, ActualShout(defaultHumn)).value
        } else {
            val operation = operations[toEval]!!.operation
            when (operation.second) {
                '+' -> eval(operation.first, values, operations, defaultHumn) + eval(operation.third, values, operations, defaultHumn)
                '-' -> eval(operation.first, values, operations, defaultHumn) - eval(operation.third, values, operations, defaultHumn)
                '*' -> eval(operation.first, values, operations, defaultHumn) * eval(operation.third, values, operations, defaultHumn)
                '/' -> eval(operation.first, values, operations, defaultHumn) / eval(operation.third, values, operations, defaultHumn)
                else -> 0
            }
        }

    private fun step2():Long {
        val input = readFile("day21.txt", 2022)
            .map { """(....): (.*)""".toRegex().find(it)!!.groupValues }
            .map { it.drop(1) }
            .map { it.zipWithNext().single() }
            .filter { it.first != "humn" }
            .partition { it.second.all { it.isDigit() } }
        val values = input.first.associateBy({ it.first }, { ActualShout(it.second.toLong()) })
        val operations =
            input.second.map { it.first to """(....) (.) (....)""".toRegex().find(it.second)!!.groupValues }
                .map { it.first to it.second.drop(1) }
                .map { it.first to Triple(it.second[0], it.second[1][0], it.second[2]) }
                .map { it.first to OperationShout(it.second) }
                .associateBy( { it.first }, { it.second } )
        val root = operations["root"]!!
        val eval2 = eval(root.operation.third, values, operations, 42L)
//        var start = 0L
//        var end = 1000000000000000L
//        val target = eval2
//        while (start < end) {
//            var mid = (end + start) / 2L
//            var result = eval(root.operation.first, values, operations, mid)
//            var delta = target - result
//            println("$start $end $mid $delta")
//            if (delta < 0) {
//                start = mid + 1L
//            } else if (delta == 0L) {
//                return mid
//            } else {
//                end = mid
//            }
//        }
//        return 0L
        return generateSequence(3560324848000L, Long::inc).dropWhile {
            val eval1 = eval(root.operation.first, values, operations, it)
            println("$it $eval1 $eval2")
            eval1 != eval2
        }.first()
        return 0L
    }

}
