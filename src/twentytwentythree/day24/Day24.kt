package twentytwentythree.day24

import readFile

class Day24 {
    fun run() {
        val input = readFile("day24.txt", 2023)
        val hailstones = parse(input)
        val mat = hailstones.first().buildMatrix(hailstones[1])
        mat.resolve().let { println(it) }
        val allMats = hailstones.buildAllMatrices()
        val sols = allMats.map { it to it.resolve() }
                .map { it.second to it.first.firstStone.computeXYforT(it.second.first) }
                .filter { it.first.areRealSolutions() }
                .filter { it.first.areFutureSolutions() }
                .filter { it.second.isSolutionWithinBounds(200000000000000.0, 400000000000000.0) }
                .count()

    }

    private fun parse(input: List<String>) = input.map { it.split('@').zipWithNext().first() }
            .map { it.first.split(',') to it.second.split(',') }
            .map { it.first.map { it.trim().toDouble() } to it.second.map { it.trim().toDouble() } }
            .map { Hailstone(it) }
}

private fun Pair<Double, Double>.isSolutionWithinBounds(min: Double, max: Double) =
        first > min && second > min && first < max && second < max

private fun Pair<Double, Double>.areFutureSolutions() = first > 0 && second > 0

private fun Pair<Double, Double>.areRealSolutions() =
    first != Double.POSITIVE_INFINITY && second != Double.POSITIVE_INFINITY && first != Double.NEGATIVE_INFINITY &&
            second != Double.NEGATIVE_INFINITY

data class Hailstone(
        val x: Double,
        val y: Double,
        val dx: Double,
        val dy: Double,
) {
    constructor(splitValues: Pair<List<Double>, List<Double>>) : this(
            splitValues.first[0],
            splitValues.first[1],
            splitValues.second[0],
            splitValues.second[1],
    )

    fun buildMatrix(other: Hailstone): Matrix {
        return Matrix(MatrixLine(dx, -1.0 * other.dx, other.x - 1.0 * x),
                MatrixLine(dy, -1.0 * other.dy, other.y - 1.0 * y),
                this, other)
    }

    fun computeXYforT(t: Double) = (x + dx * t) to (y + dy * t)
}


private fun List<Hailstone>.buildAllMatrices(): List<Matrix> {
    val result = mutableListOf<Matrix>()
    for (i in indices) {
        for (j in (i + 1..<size)) {
            result.add(this[i].buildMatrix(this[j]))
        }
    }
    return result.toList()
}

private fun Pair<MatrixLine, MatrixLine>.render(): Pair<MatrixLine, MatrixLine> {
    println("~~~")
    println("${this.first.first} , ${this.first.second} , ${this.first.third}")
    println("${this.second.first} , ${this.second.second} , ${this.second.third}")
    return this
}


data class MatrixLine(
        val first: Double,
        val second: Double,
        val third: Double,
) {
    fun divideByItselfFirst() =
            MatrixLine(first / first, second / first, third / first)

    fun divideByItselfSecond() =
            MatrixLine(first / second, second / second, third / second)
}

data class Matrix(
        val firstLine: MatrixLine,
        val secondLine: MatrixLine,
        val firstStone: Hailstone,
        val secondStone: Hailstone,
) {
    fun simplify() =
            Matrix(firstLine.divideByItselfFirst(), secondLine.divideByItselfFirst(), firstStone, secondStone)

    fun simplify2() =
            Matrix(firstLine, secondLine.divideByItselfSecond(), firstStone, secondStone)


    fun secondMinusFirst() =
            Matrix(firstLine,
                    MatrixLine(secondLine.first - firstLine.first, secondLine.second - firstLine.second, secondLine.third - firstLine.third),
                    firstStone, secondStone)

    fun resolveFirst() =
            (firstLine.third - 1 * firstLine.second * secondLine.third) to secondLine.third

    fun resolve() =
            simplify().secondMinusFirst().simplify2().resolveFirst()
}
