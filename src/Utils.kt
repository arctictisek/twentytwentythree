import java.io.File
import java.lang.Exception

fun readFile(name: String): List<String> = File("src/inputs", name).readLines()
fun readFile(name: String, year: Int): List<String> = File("src/inputs/V$year", name).readLines()
fun readFileToString(name: String, year: Int): String = File("src/inputs/V$year", name).readText()

fun String.safeGetLetter(i: Int): String =
    try { this[i].toString().filter { it.isLetter() } } catch (e: Exception) { "" }
