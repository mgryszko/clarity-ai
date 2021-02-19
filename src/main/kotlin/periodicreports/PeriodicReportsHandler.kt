package periodicreports

import kotlinx.coroutines.runBlocking
import log.Duration
import log.Host
import log.LogLine
import log.LogReader

class PeriodicReportsHandler(private val logReader: LogReader, private val collector: ReportCollector) {
    fun handle(host: Host, reportPeriod: Duration, maxTolerableLag: Duration) {
        runBlocking {
            val reportGenerator = PeriodicReportGenerator(
                host = host,
                initialTimestamp = logReader.getInitialTimestamp(),
                reportPeriod = reportPeriod,
                maxTolerableLag = maxTolerableLag
            )
            val connectedToTarget: LogLineFilter = { it.target == host }
            val onSourceConnected: LogLineAction = { collector.sourceHostConnected(it.source) }
            val actionsByFilters: Map<(LogLine) -> Boolean, (LogLine) -> Unit> = mapOf(connectedToTarget to onSourceConnected)
            logReader.readLines { line ->
                reportGenerator.processLogLine(line, collector, actionsByFilters)
            }
            collector.emitReport()
        }
    }
}