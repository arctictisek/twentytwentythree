package twentytwentythree.day5

import readFile
import kotlin.system.measureTimeMillis

class Day5 {
    fun run() {
        val input = readFile("2023-day5.txt")
        val system = parse(input)
        system.minLocation().let { println(it) }
        val timeInMillis = measureTimeMillis {
            generateSequence(1L) { it + 1L }.dropWhile { !system.mappableToSeedRange(it) }.take(1).first()
                .let { println("~~$it") }
        }
        println("The brute force took $timeInMillis ms")
    }

    private fun parse(input: List<String>) =
        System(
            input[0].replace("seeds: ", "")
                .split(' ')
                .map { it.toLong() },
            parseSpecificMap(input, "seed-to-soil"),
            parseSpecificMap(input, "soil-to-fertilizer"),
            parseSpecificMap(input, "fertilizer-to-water"),
            parseSpecificMap(input, "water-to-light"),
            parseSpecificMap(input, "light-to-temperature"),
            parseSpecificMap(input, "temperature-to-humidity"),
            parseSpecificMap(input, "humidity-to-location")
        )

    private fun parseSpecificMap(input: List<String>, map: String) =
        input.asSequence().dropWhile { it != "$map map:" }
            .drop(1)
            .takeWhile { it.isNotBlank() }
            .map { it.split(' ') }
            .map { it.map { it.toLong() } }
            .map { Range(it[0], it[1], it[2]) }.toList()
}

data class Range(
    val destinationStart: Long,
    val sourceStart: Long,
    val length: Long
) {
    infix fun containsSource(source: Long) = sourceStart <= source && source < sourceStart + length
    infix fun containsDestination(destination: Long) =
        destinationStart <= destination && destination < destinationStart + length
}

data class System(
    val seeds: List<Long>,
    val seedToSoil: List<Range>,
    val soilToFertilizer: List<Range>,
    val fertilizerToWater: List<Range>,
    val waterToLight: List<Range>,
    val lightToTemperature: List<Range>,
    val temperatureToHumidity: List<Range>,
    val humidityToLocation: List<Range>,
) {
    private fun getDestinationForSource(source: Long, map: List<Range>) =
        useRangeToGetDestination(source, map.firstOrNull { it containsSource source })

    private fun getSourceForDestination(destination: Long, map: List<Range>) =
        useRangeToGetSource(destination, map.firstOrNull { it containsDestination destination })


    private fun useRangeToGetDestination(source: Long, range: Range?) =
        if (null == range) source else
            range.destinationStart + source - range.sourceStart

    private fun useRangeToGetSource(destination: Long, range: Range?) =
        if (null == range) destination else
            range.sourceStart + destination - range.destinationStart

    private fun mapToLocation(seed: Long) =
        getDestinationForSource(seed, seedToSoil).let { soil ->
            getDestinationForSource(soil, soilToFertilizer).let { fertilizer ->
                getDestinationForSource(fertilizer, fertilizerToWater).let { water ->
                    getDestinationForSource(water, waterToLight).let { light ->
                        getDestinationForSource(light, lightToTemperature).let { temperature ->
                            getDestinationForSource(temperature, temperatureToHumidity).let { humidity ->
                                getDestinationForSource(humidity, humidityToLocation)
                            }
                        }
                    }
                }
            }
        }

    fun mappableToSeedRange(location: Long): Boolean =
        getSourceForDestination(location, humidityToLocation).let { humidity ->
            getSourceForDestination(humidity, temperatureToHumidity).let { temperature ->
                getSourceForDestination(temperature, lightToTemperature).let { light ->
                    getSourceForDestination(light, waterToLight).let { water ->
                        getSourceForDestination(water, fertilizerToWater).let { fertilizer ->
                            getSourceForDestination(fertilizer, soilToFertilizer).let { soil ->
                                getSourceForDestination(soil, seedToSoil).let { seed ->
                                    seeds.chunked(2)
                                        .map { it[0] to it[1] }
                                        .any { it.first <= seed && seed < it.first + it.second }
                                }
                            }
                        }
                    }
                }
            }
        }

    fun minLocation() = seeds.minOf { mapToLocation(it) }
}
