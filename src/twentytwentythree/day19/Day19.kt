package twentytwentythree.day19

import readFileToString

class Day19 {
    fun run() {
        val input = readFileToString("day19.txt", 2023)
        val (flows, parts) = parse(input)
        parts.filter { "A" == it.finalStop("in", flows) }.sumOf { it.sum() }.let { println(it) }
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
                                                    Condition(processedStep.first.first, processedStep.first.second),
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

    private fun Char.toFunction(n: Int): (Int) -> Boolean {
        when {
            this == '<' -> return { x -> x < n }
            this == '>' -> return { x -> x > n }
            else -> return { true }
        }
    }
}

data class Part(
    val cool: Int,
    val musical: Int,
    val aero: Int,
    val shiny: Int
) {
    constructor(properties: List<Int>) : this(properties[0], properties[1], properties[2], properties[3])
    fun nextStop(currentStop: String, workflows: Map<String, List<Rule>>): String {
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
    val predicate: (Int) -> Boolean
)
