package twentytwentythree.day20

import readFile

class Day20 {
    fun run() {
        val input = readFile("day20.txt", 2023)
        val modules = parse(input)
        repeat(1000) {
            pushButton(modules)
        }
        val result = modules.sumOf { it.lowCount() } * modules.sumOf { it.highCount() }
    }

    private fun pushButton(modules: List<Module>) {
        modules.findBroadcaster().pushButton()
        while (modules.any { it.receivedPulses().isNotEmpty() }) {
            modules.filter { it.receivedPulses().isNotEmpty() }
                    .forEach { it.processPulses(modules) }
        }
    }

    private fun parse(input: List<String>) =
            input.map { buildRawModule(it) }.also { enrichModules(it, input) }

    private fun enrichModules(modules: List<Module>, input: List<String>) {
        input.onEach { enrichModuleWithTargets(it, modules) }
        modules.filterIsInstance<Conjunction>().forEach { enrichConjunction(it, modules) }
    }

    private fun enrichConjunction(conjunction: Conjunction, modules: List<Module>) {
        modules.filter { it.targets().contains(conjunction.name) }
                .map { it.name() }
                .associateWith { Pulse.LOW }
                .let { conjunction.lastReceivedPulses.putAll(it) }
    }

    private fun enrichModuleWithTargets(line: String, modules: List<Module>) {
        line.split("-> ")
                .drop(1)
                .take(1)
                .first()
                .split(",")
                .map { it.trim() }
                .let { ((modules.first { it.name() == line.getModuleName() }).targets().addAll(it)) }
    }

    private fun buildRawModule(line: String) =
            when {
                line.startsWith("broadcaster") -> Broadcaster()
                line.startsWith('%') -> FlipFlop(line.split(" ").first().substring(1))
                else -> Conjunction(line.split(" ").first().substring(1))
            }

    private fun String.getModuleName() =
            if (this.startsWith("broadcaster")) "broadcaster"
            else this.split(" ").first().substring(1)
}

private fun List<Module>.findBroadcaster() = this.first { it.name() == "broadcaster" } as Broadcaster

sealed class Module {
    abstract fun name(): String
    abstract fun targets(): MutableList<String>
    abstract fun receivedPulses(): MutableList<ReceivedPulse>
    abstract fun processPulses(modules: List<Module>)
    abstract fun lowCount(): Int
    abstract fun highCount(): Int
}

data class Broadcaster(
        val targets: MutableList<String> = mutableListOf(),
        val receivedPulses: MutableList<ReceivedPulse> = mutableListOf(),
        var lowPulsesSent: Int = 0,
) : Module() {
    override fun name() = "broadcaster"
    override fun targets() = targets
    override fun receivedPulses() = receivedPulses
    fun pushButton() {
        ++lowPulsesSent
        receivedPulses.add(ReceivedPulse("", Pulse.LOW, 0))
    }

    override fun processPulses(modules: List<Module>) {
        receivedPulses.forEach {
            targets.mapIndexed { index, target ->
                modules.first { it.name() == target }.receivedPulses().add(ReceivedPulse("broadcaster", it.pulse, index))
            }
        }
        receivedPulses.clear()
        lowPulsesSent += targets.size
    }

    override fun lowCount() = lowPulsesSent
    override fun highCount() = 0
}

data class FlipFlop(
        val name: String,
        val targets: MutableList<String> = mutableListOf(),
        var state: Boolean = false,
        val receivedPulses: MutableList<ReceivedPulse> = mutableListOf(),
        var lowPulsesSent: Int = 0,
        var highPulsesSent: Int = 0,
) : Module() {
    override fun name() = name
    override fun targets() = targets
    override fun receivedPulses() = receivedPulses
    override fun processPulses(modules: List<Module>) {
        receivedPulses.forEach {
            if (it.pulse == Pulse.HIGH) {
                // ignore
            } else {
                if (state) {
                    targets.mapIndexed { index, target ->
                        ++lowPulsesSent
                        modules.firstOrNull { it.name() == target }?.receivedPulses()?.add(ReceivedPulse(this.name, Pulse.LOW, index))
                    }
                } else {
                    targets.mapIndexed { index, target ->
                        ++highPulsesSent
                        modules.firstOrNull { it.name() == target }?.receivedPulses()?.add(ReceivedPulse(this.name, Pulse.HIGH, index))
                    }
                }
                state = !state
            }
        }
        receivedPulses.clear()
    }
    override fun lowCount() = lowPulsesSent
    override fun highCount() = highPulsesSent
}

data class Conjunction(
        val name: String,
        val lastReceivedPulses: MutableMap<String, Pulse> = mutableMapOf(),
        val targets: MutableList<String> = mutableListOf(),
        val receivedPulses: MutableList<ReceivedPulse> = mutableListOf(),
        var lowPulsesSent: Int = 0,
        var highPulsesSent: Int = 0,
) : Module() {
    override fun name() = name
    override fun targets() = targets
    override fun receivedPulses() = receivedPulses
    override fun processPulses(modules: List<Module>) {
        receivedPulses.forEach {
            lastReceivedPulses[it.origin] = it.pulse
        }
        receivedPulses.clear()
        if (lastReceivedPulses.values.all { it == Pulse.HIGH } ) {
            targets.mapIndexed { index, target ->
                ++lowPulsesSent
                modules.firstOrNull { it.name() == target }?.receivedPulses()?.add(ReceivedPulse(this.name, Pulse.LOW, index))
            }
        } else {
            targets.mapIndexed { index, target ->
                ++highPulsesSent
                modules.firstOrNull { it.name() == target }?.receivedPulses()?.add(ReceivedPulse(this.name, Pulse.HIGH, index))
            }
        }
    }
    override fun lowCount() = lowPulsesSent
    override fun highCount() = highPulsesSent
}


enum class Pulse {
    HIGH, LOW
}

data class ReceivedPulse(
        val origin: String,
        val pulse: Pulse,
        val order: Int,
)
