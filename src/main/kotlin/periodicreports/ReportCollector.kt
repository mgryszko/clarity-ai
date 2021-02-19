package periodicreports

import log.Host

data class Report(val sources: Set<Host>)

class ReportCollector(private val onReportReady: ((Report) -> Unit)) {
    private val sourceHosts = mutableSetOf<Host>()
    private val emptyReport = Report(sources = emptySet())

    fun sourceHostConnected(source: Host) {
        sourceHosts.add(source)
    }

    fun emitReport() {
        onReportReady(Report(sources = sourceHosts.toSet()))
        sourceHosts.clear()
    }

    @Suppress("ForEachParameterNotUsed")
    fun emitEmptyReports(count: Long) {
        (0 until count).forEach {
            onReportReady(emptyReport)
        }
    }
}
