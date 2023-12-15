package twentytwentythree.day15

import readFileToString

class Day15 {
    fun run() {
        val input = readFileToString("day15.txt", 2023)
        input.trim().computeHashOfInitialisationSequence().also { println(it) }

        mutableMapOf<Int, MutableList<Lens>>().let { boxes ->
            handleAllOperations(input, boxes)
            compute(boxes).also { println(it) }
        }
    }

    private fun Char.hashValue(currentValue: Int = 0) =
        ((currentValue + this.code) * 17) % 256

    private fun String.hashValue() = this.fold(0) { acc, c -> c.hashValue(acc) }

    private fun String.computeHashOfInitialisationSequence() = this.split(',').sumOf { it.hashValue() }

    private fun computeOnList(boxNumber: Int, listOfLenses: List<Lens>): Int {
        return listOfLenses.mapIndexed { index, lens -> (1 + boxNumber) * (index + 1) * lens.focalLength }.sum()
    }

    private fun compute(boxes: MutableMap<Int, MutableList<Lens>>): Int {
        return boxes.map { (boxNumber, listOfLenses) -> computeOnList(boxNumber, listOfLenses) }
                .sum()
    }

    private fun handleAllOperations(operations: String, boxes: MutableMap<Int, MutableList<Lens>>) {
        operations.trim().split(',').forEach { handleOperation(it, boxes) }
    }

    private fun handleOperation(operation: String, boxes: MutableMap<Int, MutableList<Lens>>) {
        if (operation.contains('-')) {
            val labelOfLensToRemove = operation.split('-').first()
            val boxNumber = labelOfLensToRemove.hashValue()
            boxes[boxNumber]?.removeIf { it.label == labelOfLensToRemove }
        } else {
            val lensToAdd = operation.split('=').let { Lens(it[0], it[1].toInt()) }
            val boxNumber = lensToAdd.label.hashValue()
            if (boxes.keys.contains(boxNumber)) {
                if (boxes[boxNumber]!!.any { it.label == lensToAdd.label }) {
                    boxes[boxNumber]!!.first { it.label == lensToAdd.label }.focalLength = lensToAdd.focalLength
                } else {
                    boxes[boxNumber]!!.add(lensToAdd)
                }
            } else {
                boxes[boxNumber] = mutableListOf(lensToAdd)
            }
        }
    }
}

data class Lens(
        val label: String,
        var focalLength: Int
)
