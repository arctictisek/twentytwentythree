package twentytwentythree.day19

import readFileToString
import kotlin.math.max
import kotlin.math.min

class Day19 {
    fun run() {
        val input = readFileToString("day19.txt", 2023)
        val (flows, parts) = parse(input)
//        parts.filter { "A" == it.finalStop("in", flows) }.sumOf { it.sum() }.let { println(it) }

        val visited = mutableListOf<String>()
        val currentPath = mutableListOf<String>()
        val allPaths = mutableListOf<MutableList<String>>()
        listAllPaths("in", "A", flows, visited, currentPath, allPaths)

        val pairs = allPaths.flatMap { it.zipWithNext() }
                .distinct()
                .flatMap { buildTransition(it.first, it.second, flows) }
        val allTransitions = allPaths.map {
            it.zipWithNext()
                    .map { (origin, destination) -> pairs.first { it.origin == origin && it.destination == destination } }
                    .reduce { acc, transition -> acc.combineWith(transition) }
        }.distinct()

        val allRanges = allTransitions.map { transition ->
            val range = AcceptableRange()
            range.applyAllConditions(transition.conditions)
            range.cardinal()
        }.sum()
        val x = 0

    }

    private fun parse(input: String): Pair<Map<String, List<Rule>>, List<Part>> {
        return input.split("\n\n")
                .take(2)
                .zipWithNext()
                .first()
                .let { (flows, parts) ->
                    flows.split('\n')
                            .filter { it.isNotBlank() }
                            .map { flow -> flow.split('{') }
                            .associate {
                                it[0] to
                                        it[1].replace("}", "")
                                                .split(',')
                                                .map { ruleString ->
                                                    if (ruleString.contains(':')) {
                                                        ruleString.split(':').let { splitRule ->
                                                            splitRule[0].let { condition ->
                                                                condition[0] to condition[1].toFunction(
                                                                        condition.substring(2).toInt()
                                                                )
                                                            } to
                                                                    splitRule[1]
                                                        }.let { processedStep ->
                                                            Rule(
                                                                    Condition(processedStep.first.first, processedStep.first.second.first, processedStep.first.second.second.second, processedStep.first.second.second.first),
                                                                    processedStep.second
                                                            )
                                                        }
                                                    } else {
                                                        Rule(null, ruleString)
                                                    }
                                                }
                            } to
                            parts.split('\n')
                                    .filter { it.isNotBlank() }
                                    .map { part -> part.substring(1) }
                                    .map { strippedOfHeadingBrace -> strippedOfHeadingBrace.replace("}", "") }
                                    .map { strippedOfTrailingBrace ->
                                        strippedOfTrailingBrace.split(',')
                                                .map { properties -> properties.split('=')[1].toInt() }
                                                .let { Part(it) }
                                    }
                }
    }

    private fun listAllPaths(origin: String,
                             destination: String,
                             flows: Map<String, List<Rule>>,
                             visited: MutableList<String>,
                             currentPath: MutableList<String>,
                             allPaths: MutableList<MutableList<String>>) {
        if (visited.contains(origin)) return
        visited.add(origin)
        currentPath.add(origin)
        if (origin == destination) {
            allPaths.add(currentPath.toMutableList())
            visited.remove(origin)
            currentPath.removeLast()
            return
        }
        if (flows.keys.contains(origin)) {
            flows[origin]!!.map { it.destination }
                    .forEach { listAllPaths(it, destination, flows, visited, currentPath, allPaths) }
        }
        currentPath.removeLast()
        visited.remove(origin)
    }

    private fun Char.toFunction(n: Int): Pair<(Int) -> Boolean, Pair<Char, Int>> {
        when {
            this == '<' -> return { x: Int -> x < n } to (this to n)
            this == '>' -> return { x: Int -> x > n } to (this to n)
            else -> return { _: Int -> true } to (this to n)
        }
    }

//    private fun buildTransition(origin: String, destination: String, flows: Map<String, List<Rule>>): List<Transition> {
//        val rules = flows[origin]!!
//        val conditions = mutableListOf<SimplifiedCondition>()
//
//        for (rule in rules) {
//            if (rule.destination == destination) {
//                if (rule.condition != null) {
//                    if (rules.any { it.condition == null && it.destination == destination }) {
//                        continue
//                    } else {
//                        conditions.add(rule.condition.simplify())
//                        break
//                    }
//                }
//            } else {
//                if (rule.condition != null) conditions.add(rule.condition.invert())
//            }
//        }
//        return listOf(Transition(origin, destination, conditions))
//    }

    private fun buildTransition(origin: String, destination: String, flows: Map<String, List<Rule>>): List<Transition> {
        val rules = flows[origin]!!
        val prunedRules = pruneA(rules)
        val destRules = prunedRules.filter { it.destination == destination }
        var conditions = mutableListOf<SimplifiedCondition>()
        val result = mutableListOf<Transition>()

        for (destRule in destRules) {
            conditions = mutableListOf()
            for (rule in prunedRules) {
                if (rule == destRule) {
                    if (rule.condition != null) {
                        conditions.add(rule.condition.simplify())
                        break
                    }
                } else {
                    if (rule.condition != null) conditions.add(rule.condition.invert())
                }
            }
            result.add(Transition(origin, destination, conditions.toList()))
        }
        return result
    }

    private fun pruneA(rules: List<Rule>): List<Rule> {
        if (rules.last().condition == null &&
                rules.last().destination == "A" &&
                rules.size > 1) {
            if (rules[rules.size - 2].destination == "A") {
                return pruneA(rules.subList(0, rules.size - 2) + listOf(rules.last()))
            }
        }
        return rules
    }
}

data class Part(
        val cool: Int,
        val musical: Int,
        val aero: Int,
        val shiny: Int
) {
    constructor(properties: List<Int>) : this(properties[0], properties[1], properties[2], properties[3])

    private fun nextStop(currentStop: String, workflows: Map<String, List<Rule>>): String {
        workflows[currentStop]!!.forEach {
            if (null == it.condition) return it.destination
            else {
                val toUse = when (it.condition.property) {
                    'x' -> this.cool
                    'm' -> this.musical
                    'a' -> this.aero
                    else -> this.shiny
                }
                if (it.condition.predicate(toUse)) return it.destination
            }
        }
        return ""
    }

    tailrec fun finalStop(curStop: String, workflows: Map<String, List<Rule>>): String =
            if (setOf("A", "R").contains(curStop)) curStop
            else finalStop(nextStop(curStop, workflows), workflows)

    fun sum(): Int = cool + musical + aero + shiny
}

data class Rule(
        val condition: Condition?,
        val destination: String
)

data class Condition(
        val property: Char,
        val predicate: (Int) -> Boolean,
        val boundary: Int,
        val operation: Char
) {
    fun simplify() = SimplifiedCondition(boundary, operation, property)
    fun invert(): SimplifiedCondition =
            when (operation) {
                '<' -> SimplifiedCondition(boundary - 1, '>', property)
                else -> SimplifiedCondition(boundary + 1, '<', property)
            }
}

data class SimplifiedCondition(
        val boundary: Int,
        val operation: Char,
        val property: Char
)

data class Transition(
        val origin: String,
        val destination: String,
        val conditions: List<SimplifiedCondition>
) {
    fun combineWith(transition: Transition): Transition {
        return Transition(origin, transition.destination, conditions + transition.conditions)
    }
}

data class AcceptableRange(
        var minX: Int = 1,
        var maxX: Int = 4000,
        var minM: Int = 1,
        var maxM: Int = 4000,
        var minA: Int = 1,
        var maxA: Int = 4000,
        var minS: Int = 1,
        var maxS: Int = 4000
) {
    companion object {
        private fun calculateCondition(operation: Char, boundary: Int, minMax: Pair<Int, Int>) =
                when (operation) {
                    '<' -> minMax.first to min(minMax.second, boundary - 1)
                    else -> max(minMax.first, boundary + 1) to minMax.second
                }
    }

    fun cardinal() = (maxX - minX + 1).toLong() *
            (maxM - minM + 1).toLong() *
            (maxA - minA + 1).toLong() *
            (maxS - minS + 1).toLong()

    fun applyAllConditions(simplifiedConditions: List<SimplifiedCondition>) =
            simplifiedConditions.forEach { applySimplifiedCondition(it) }

    fun applySimplifiedCondition(simplifiedCondition: SimplifiedCondition) {
        when (simplifiedCondition.property) {
            'x' -> calculateCondition(simplifiedCondition.operation, simplifiedCondition.boundary, minX to maxX).let { xy ->
                minX = xy.first
                maxX = xy.second
            }

            'm' -> calculateCondition(simplifiedCondition.operation, simplifiedCondition.boundary, minM to maxM).let { xy ->
                minM = xy.first
                maxM = xy.second
            }

            'a' -> calculateCondition(simplifiedCondition.operation, simplifiedCondition.boundary, minA to maxA).let { xy ->
                minA = xy.first
                maxA = xy.second
            }

            else -> calculateCondition(simplifiedCondition.operation, simplifiedCondition.boundary, minS to maxS).let { xy ->
                minS = xy.first
                maxS = xy.second
            }
        }
        val dd = cardinal()
        val r = 0
    }
}
