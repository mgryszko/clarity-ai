package periodicreports

import log.Host

data class Report(val sources: Set<Host>)

interface ReportEmitter {
    fun emitReport()

    fun emitEmptyReports(count: Long)
}

class ReportCollector(private val onReportReady: ((Report) -> Unit)) : ReportEmitter {
    private val sourceHosts = mutableSetOf<Host>()
    private val emptyReport = Report(sources = emptySet())

    fun sourceHostConnected(source: Host) {
        sourceHosts.add(source)
    }

    override fun emitReport() {
        onReportReady(Report(sources = sourceHosts.toSet()))
        sourceHosts.clear()
    }

    @Suppress("ForEachParameterNotUsed")
    override fun emitEmptyReports(count: Long) {
        (0 until count).forEach {
            onReportReady(emptyReport)
        }
    }
}
