import java.io.File
import java.lang.Exception

fun readFile(name: String): List<String> = File("src/inputs", name).readLines()
fun readFileToString(name: String): String = File("src/inputs", name).readText()

fun String.safeGetLetter(i: Int): String =
    try { this[i].toString().filter { it.isLetter() } } catch (e: Exception) { "" }
