package twentytwentytwo.day7

import readFile

class Day7 {
    fun run() {
        println(step1())
        println(step2())
    }

    private fun step1(): Int {
        val directory = parseInput("day7.txt")
        directory.size()
        return sumSub100000(directory)
    }

    private fun step2(): Int {
        val directory = parseInput("day7.txt")
        val needToFree = directory.size() - 40000000
        return directoryCollection(directory).map { it.totalSize }.filter { it >= needToFree }.min()
    }

    private fun directoryCollection(directory: Directory): Collection<Directory> {
        return listOf(directory) + directory.children.flatMap { directoryCollection(it) }
    }

    private fun parseInput(filename: String): Directory {
        val root = Directory(null, "/", mutableListOf(), mutableListOf())
        var currentDirectory = root
        for (line in readFile(filename, 2022).drop(1)) {
            if (isCommand(line)) {
                currentDirectory = handleCommand(line, currentDirectory)
            } else {
                handleFileLine(currentDirectory, line)
            }
        }
        return root
    }

    private fun handleFileLine(currentDirectory: Directory, line: String) {
        val (sizeOrDir, fileName) = line.split(' ')
        if (sizeOrDir == "dir") {
            currentDirectory.children.add(Directory(currentDirectory, fileName, mutableListOf(), mutableListOf()))
        } else {
            currentDirectory.files.add(File(fileName, sizeOrDir.toInt()))
        }
    }

    private fun handleCommand(line: String, currentDirectory: Directory): Directory {
        val splitLine = line.split(' ').drop(1)
        return if (splitLine.first() == "ls") {
            currentDirectory
        } else {
            if (splitLine[1] == "..") {
                currentDirectory.parent!!
            } else {
                currentDirectory.children.first { it.name == splitLine[1] }
            }
        }
    }

    private fun isCommand(line: String): Boolean = line.first() == '$'

    private fun sumSub100000(directory: Directory): Int {
        return if (directory.totalSize <= 100000) {
            directory.totalSize + directory.children.sumOf { sumSub100000(it) }
        } else {
            directory.children.sumOf { sumSub100000(it) }
        }
    }
}
