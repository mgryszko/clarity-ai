package periodicreports

import log.Host

class ReportCollector(private val onReportReady: ((Set<Host>) -> Unit)) {
    private val sourceHosts = mutableSetOf<Host>()

    fun sourceHostConnected(source: Host) {
        sourceHosts.add(source)
    }

    fun closeReport() {
        onReportReady(sourceHosts.toSet())
        sourceHosts.clear()
    }

    @Suppress("ForEachParameterNotUsed")
    fun closeEmptyReports(count: Long) {
        (0 until count).forEach {
            onReportReady(emptySet())
        }
    }
}
