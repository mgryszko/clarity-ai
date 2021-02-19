package periodicreports

import kotlinx.coroutines.runBlocking
import log.Duration
import log.LogReader

class PeriodicReportsHandler(
    private val logReader: LogReader,
    private val collector: ReportCollector,
    private val actionsByFilters: Map<LogLineFilter, LogLineAction>
) {
    fun handle(reportPeriod: Duration, maxTolerableLag: Duration) {
        runBlocking {
            val reportGenerator = PeriodicReportGenerator(
                actionsByFilters = actionsByFilters,
                initialTimestamp = logReader.getInitialTimestamp(),
                reportPeriod = reportPeriod,
                maxTolerableLag = maxTolerableLag
            )
            logReader.readLines { line ->
                reportGenerator.processLogLine(line, collector)
            }
            collector.emitReport()
        }
    }
}