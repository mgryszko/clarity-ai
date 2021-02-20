package periodicreports

import log.Host
import log.Timestamp
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

    fun topCount(): Int = keysByCounters.lastEntry()?.key ?: 0

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

    fun outgoingConnectionFrom(source: Host) {
        outgoingConnectionsFromSources.add(source)
    }

    fun incomingConnectionTo(target: Host) {
        incomingConnectionsToTargets.add(target)
    }

    fun topOutgoingConnections(source: Host) {
        topOutgoingConnections.increment(source)
    }

    override fun emitReport(timestamp: Timestamp) {
        renderer.render(
            ConnectionReport(
                timestamp = timestamp,
                incomingFrom = outgoingConnectionsFromSources.toSet(),
                outgoingTo = incomingConnectionsToTargets.toSet(),
                topOutgoing = topOutgoingConnections.topKeys(),
                topOutgoingNumber = topOutgoingConnections.topCount(),
            )
        )
        outgoingConnectionsFromSources.clear()
        incomingConnectionsToTargets.clear()
        topOutgoingConnections.clear()
    }

    override fun emitEmptyReport(timestamp: Timestamp) {
        renderer.render(ConnectionReport(timestamp = timestamp))
    }
}
