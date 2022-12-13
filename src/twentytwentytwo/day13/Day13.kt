package twentytwentytwo.day13

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import readFile
import twentytwentytwo.day13.Comparison.YES
import twentytwentytwo.day13.Comparison.NO
import twentytwentytwo.day13.Comparison.INDECISIVE

class Day13 {
    fun run() {
        println(step1())
        println(step2())
    }

    private fun step1(): Int =
        createInputStep1("day13.txt").mapIndexed { i, l -> (i + 1) to compareAny(l) }
            .filter { it.second == YES }
            .sumOf { it.first }

    private fun createInputStep1(file: String) = jacksonObjectMapper().let { mapper ->
        readFile(file)
            .chunked(3)
            .map { it[0] to it[1] }
            .map { mapper.readValue<List<*>>(it.first) to mapper.readValue<List<*>>(it.second) }
    }

    private fun createInputStep2(file: String) = jacksonObjectMapper().let { mapper ->
        ((readFile(file).filter { it.contains('[') }) + listOf("[[2]]", "[[6]]"))
            .map { mapper.readValue<List<*>>(it) }
    }


    public fun compareAny(pair: Pair<Any?, Any?>): Comparison {
        if (pair.first is Int && pair.second is List<*>) {
            return compareIntAndList(pair.first as Int to pair.second as List<*>)
        } else if (areBothNonEmptyIntLists(pair)) {
            return compareLists(pair.first as List<Int> to pair.second as List<Int>)
        } else if (pair.first is List<*> && pair.second is Int) {
            return compareListAndInt(pair.first as List<*> to pair.second as Int)
        } else if (pair.first is List<*> && pair.second is List<*>) {
            return compareAnyList(pair.first as List<*> to pair.second as List<*>)
        } else if (pair.first is Int && pair.second is Int) {
            return compareInts(pair.first as Int to pair.second as Int)
        }
        return INDECISIVE
    }

    private fun areBothNonEmptyIntLists(pair: Pair<Any?, Any?>) =
        pair.first is List<*> && (pair.first as List<*>).isNotEmpty() && (pair.first as List<*>).all { it is Int } &&
                pair.second is List<*> && (pair.second as List<*>).isNotEmpty() && (pair.second as List<*>).all { it is Int }

    private fun compareAnyList(pair: Pair<List<*>, List<*>>): Comparison {
        var i = 0
        while (i < pair.first.size && i < pair.second.size &&
            INDECISIVE == compareAny(pair.first[i] to pair.second[i])
        ) {
            ++i
        }
        if (listOf(pair.first.size, pair.second.size).contains(i)) {
            return if (pair.first.size < pair.second.size) {
                YES
            } else if (pair.first.size > pair.second.size) {
                NO
            } else {
                INDECISIVE
            }
        }
        if (INDECISIVE != compareAny(pair.first[i] to pair.second[i])) {
            return compareAny(pair.first[i] to pair.second[i])
        }
        return INDECISIVE
    }

    private fun compareLists(pair: Pair<List<Int>, List<Int>>): Comparison {
        var i = 0
        while (i < pair.first.size && i < pair.second.size &&
            INDECISIVE == compareInts(pair.first[i] to pair.second[i])
        ) {
            ++i
        }
        if (listOf(pair.first.size, pair.second.size).contains(i)) {
            return if (pair.first.size < pair.second.size) {
                YES
            } else if (pair.first.size > pair.second.size) {
                NO
            } else {
                INDECISIVE
            }
        }
        if (INDECISIVE != compareInts(pair.first[i] to pair.second[i])) {
            return compareInts(pair.first[i] to pair.second[i])
        }
        return INDECISIVE
    }

    private fun compareInts(pair: Pair<Int, Int>): Comparison {
        return when {
            pair.first < pair.second -> YES
            pair.first > pair.second -> NO
            else -> INDECISIVE
        }
    }

    private fun compareIntAndList(pair: Pair<Int, List<*>>): Comparison =
        compareAnyList(listOf(pair.first) to pair.second)

    private fun compareListAndInt(pair: Pair<List<*>, Int>): Comparison =
        compareAnyList(pair.first to listOf(pair.second))

    private fun step2(): Int {
        val input = createInputStep2("day13.txt")
        val sorted = input.sortedWith(CompareLists)
            .mapIndexed { index, anies -> index to anies }
            .filter { it.second == listOf(listOf(6)) || it.second == listOf(listOf(2)) }
            .map { it.first }
            .map { it + 1 }
            .zipWithNext()
            .single()
            .let { it.first * it.second }
        return sorted
    }

    class CompareLists {
        companion object: Comparator<List<*>> {
            override fun compare(o1: List<*>?, o2: List<*>?): Int {
                return Day13().compareAny(o1 to o2).let {
                    when (it) {
                        YES -> -1
                        NO -> 1
                        INDECISIVE -> 0
                    }
                }
            }
        }
    }
}

enum class Comparison {
    YES, NO, INDECISIVE
}
