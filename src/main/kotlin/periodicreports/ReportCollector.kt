package periodicreports

import log.Host

class ReportCollector(private val onReportReady: ((Set<Host>) -> Unit)) {
    private val sourceHosts = mutableSetOf<Host>()

    fun sourceHostConnected(source: Host) {
        sourceHosts.add(source)
    }

    fun emitReport() {
        onReportReady(sourceHosts.toSet())
        sourceHosts.clear()
    }

    @Suppress("ForEachParameterNotUsed")
    fun emitEmptyReports(count: Long) {
        (0 until count).forEach {
            onReportReady(emptySet())
        }
    }
}
