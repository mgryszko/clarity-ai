package periodicreports

import log.Host
import java.util.*

data class Report(val sources: Set<Host> = emptySet(), val targets: Set<Host> = emptySet(), val topSourceConnections: Host? = null)

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

    fun topKey(): K? = keysByCounters.lastEntry()?.value?.firstOrNull()

    fun clear() {
        countersByKeys.clear()
        keysByCounters.clear()
    }

    override fun toString(): String = "TopCounter(keysByCounters=$keysByCounters, countersByKeys=$countersByKeys)"
}

interface ReportEmitter {
    fun emitReport()

    fun emitEmptyReports(count: Long)
}

class ReportCollector(private val onReportReady: ((Report) -> Unit)) : ReportEmitter {
    private val sourceHosts = mutableSetOf<Host>()
    private val targetHosts = mutableSetOf<Host>()
    private val topOutgoingConnections = TopCounter<Host>()
    private val emptyReport = Report()

    fun sourceHostConnected(source: Host) {
        sourceHosts.add(source)
    }

    fun connectedToTarget(target: Host) {
        targetHosts.add(target)
    }

    fun topOutgoingConnections(source: Host) {
        topOutgoingConnections.increment(source)
    }

    override fun emitReport() {
        onReportReady(Report(sources = sourceHosts.toSet(), targets = targetHosts.toSet(), topSourceConnections = topOutgoingConnections.topKey()))
        sourceHosts.clear()
        targetHosts.clear()
        topOutgoingConnections.clear()
    }

    @Suppress("ForEachParameterNotUsed")
    override fun emitEmptyReports(count: Long) {
        (0 until count).forEach {
            onReportReady(emptyReport)
        }
    }
}
