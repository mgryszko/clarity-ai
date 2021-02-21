package periodicreports

import log.Duration
import log.LogReader

class PeriodicReportsHandler(
    private val logReader: LogReader,
    private val emitter: ReportEmitter,
    private val actionsByFilters: Map<LogLineFilter, LogLineAction>
) {
    suspend fun handle(reportPeriod: Duration, maxTolerableLag: Duration) {
        val reportGenerator = PeriodicReportGenerator(
            emitter = emitter,
            actionsByFilters = actionsByFilters,
            reportPeriod = reportPeriod,
            maxTolerableLag = maxTolerableLag
        )
        logReader.readLines { line ->
            reportGenerator.processLogLine(line)
        }
        reportGenerator.noMoreLines()
    }
}