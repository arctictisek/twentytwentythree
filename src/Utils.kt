import java.io.File

fun readFile(name: String): List<String> = File("src/inputs", name).readLines()
