package periodicreports

import log.Host
import java.util.*

// Data structure allowing counting and retrieving the top key in log(n) time
private class TopCounter<K> {
    private val keysByCounters = TreeMap<Int, Set<K>>()
    private val countersByKeys = mutableMapOf<K, Int>()

    fun increment(key: K) {
        val count = countersByKeys.getOrDefault(key, 0)

        countersByKeys[key] = count + 1

        removeKeyFromCounter(key, count)
        addKeyToCounter(key, count + 1)
    }

    private fun addKeyToCounter(key: K, count: Int) {
        keysByCounters.putIfAbsent(count, emptySet())
        keysByCounters.computeIfPresent(count) { _, keys -> keys + key }
    }

    private fun removeKeyFromCounter(key: K, count: Int) {
        val keys = keysByCounters.getOrDefault(count, emptySet())
        // optimization - don't keep empty sets in memory
        (keys - key).let {
            if (it.isEmpty()) {
                keysByCounters.remove(count)
            } else {
                keysByCounters[count] = it
            }
        }
    }

    fun topKeys(): Set<K> = keysByCounters.lastEntry()?.value ?: emptySet()

    fun clear() {
        countersByKeys.clear()
        keysByCounters.clear()
    }

    override fun toString(): String = "TopCounter(keysByCounters=$keysByCounters, countersByKeys=$countersByKeys)"
}

class ReportCollector(private val renderer: ReportRenderer) : ReportEmitter {
    private val outgoingConnectionsFromSources = mutableSetOf<Host>()
    private val incomingConnectionsToTargets = mutableSetOf<Host>()
    private val topOutgoingConnections = TopCounter<Host>()
    private val emptyReport = ConnectionReport()

    fun outgoingConnectionFrom(source: Host) {
        outgoingConnectionsFromSources.add(source)
    }

    fun incomingConnectionTo(target: Host) {
        incomingConnectionsToTargets.add(target)
    }

    fun topOutgoingConnections(source: Host) {
        topOutgoingConnections.increment(source)
    }

    override fun emitReport() {
        renderer.render(
            ConnectionReport(
                incomingFrom = outgoingConnectionsFromSources.toSet(),
                outgoingTo = incomingConnectionsToTargets.toSet(),
                topOutgoing = topOutgoingConnections.topKeys()
            )
        )
        outgoingConnectionsFromSources.clear()
        incomingConnectionsToTargets.clear()
        topOutgoingConnections.clear()
    }

    @Suppress("ForEachParameterNotUsed")
    override fun emitEmptyReports(count: Long) {
        (0 until count).forEach {
            renderer.render(emptyReport)
        }
    }
}
