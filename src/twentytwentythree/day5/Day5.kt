package twentytwentythree.day5

import readFile

class Day5 {
    fun run() {
        val input = readFile("2023-day5.txt")
        val system = parse(input)
        system.minLocation().let { println(it) }
        //331445006
    }

    private fun parse(input: List<String>) =
            System(input[0].replace("seeds: ", "")
                    .split(' ')
                    .map { it.toLong() },
                    parseSpecificMap(input, "seed-to-soil"),
                    parseSpecificMap(input, "soil-to-fertilizer"),
                    parseSpecificMap(input, "fertilizer-to-water"),
                    parseSpecificMap(input, "water-to-light"),
                    parseSpecificMap(input, "light-to-temperature"),
                    parseSpecificMap(input, "temperature-to-humidity"),
                    parseSpecificMap(input, "humidity-to-location"))

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
    infix fun containsSource(source: Long) =
            (sourceStart until sourceStart + length).contains(source)
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

    private fun useRangeToGetDestination(source: Long, range: Range?) =
            if (null == range) source else
                range.destinationStart + source - range.sourceStart

    private fun mapToLocation(seed: Long): Long {
        println("Seed $seed")
        val soil = getDestinationForSource(seed, seedToSoil).also { println("Soil: $it") }
        val fertilizer = getDestinationForSource(soil, soilToFertilizer).also { println("Fertilize: $it") }
        val water = getDestinationForSource(fertilizer, fertilizerToWater).also { println("Water: $it") }
        val light = getDestinationForSource(water, waterToLight).also { println("Light: $it") }
        val temperature = getDestinationForSource(light, lightToTemperature).also { println("Temperature: $it") }
        val humidity = getDestinationForSource(temperature, temperatureToHumidity).also { println("Humidity: $it") }
        return getDestinationForSource(humidity, humidityToLocation).also { println("Location: $it") }
    }

    fun mapRangeToMinLocation(range: Pair<Long, Long>) =
            (range.first until range.first + range.second).minOf { mapToLocation(it) }

    private fun mapAllSeedsToLocations() =
        seeds.map { mapToLocation(it) }

    fun minLocation() = mapAllSeedsToLocations().min()

    fun seedsRanges() = seeds.zipWithNext()
}
