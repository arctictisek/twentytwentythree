package twentytwentytwo.day13

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import readFile


class CreateInput {

    private val mapper = jacksonObjectMapper()

    fun createInput(file: String) = readFile(file)
        .chunked(3)
        .map { it[0] to it[1] }
        .map { mapper.readValue<List<*>>(it.first) to mapper.readValue<List<*>>(it.second) }
}
