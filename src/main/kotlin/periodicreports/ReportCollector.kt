package periodicreports

import log.Host
import log.Timestamp

private class TopCounter<K> {
    private val countersByKeys = mutableMapOf<K, Int>()

    fun increment(key: K) {
        val count = countersByKeys.getOrDefault(key, 0)
        countersByKeys[key] = count + 1
    }

    fun topCount(): Int = sortedCountersByKeys().firstOrZero()

    fun topKeys(): Set<K>  {
        val sorted = sortedCountersByKeys()
        val topCount = sorted.firstOrZero()
        return sorted.takeWhile { it.value == topCount }.map { it.key }.toSet()
    }

    private fun <K> List<Map.Entry<K, Int>>.firstOrZero(): Int = this.firstOrNull()?.value ?: 0

    private fun sortedCountersByKeys(): List<Map.Entry<K, Int>> = countersByKeys.entries.sortedByDescending { it.value }

    fun clear() {
        countersByKeys.clear()
    }

    override fun toString(): String = "TopCounter(countersByKeys=$countersByKeys)"
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
