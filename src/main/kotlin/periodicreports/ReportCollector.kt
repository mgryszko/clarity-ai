package periodicreports

import log.Host

data class Report(val sources: Set<Host> = emptySet(), val targets: Set<Host> = emptySet())

interface ReportEmitter {
    fun emitReport()

    fun emitEmptyReports(count: Long)
}

class ReportCollector(private val onReportReady: ((Report) -> Unit)) : ReportEmitter {
    private val sourceHosts = mutableSetOf<Host>()
    private val targetHosts = mutableSetOf<Host>()
    private val emptyReport = Report(sources = emptySet())

    fun sourceHostConnected(source: Host) {
        sourceHosts.add(source)
    }

    fun connectedToTarget(target: Host) {
        targetHosts.add(target)
    }

    override fun emitReport() {
        onReportReady(Report(sources = sourceHosts.toSet(), targets = targetHosts.toSet()))
        sourceHosts.clear()
        targetHosts.clear()
    }

    @Suppress("ForEachParameterNotUsed")
    override fun emitEmptyReports(count: Long) {
        (0 until count).forEach {
            onReportReady(emptyReport)
        }
    }
}
