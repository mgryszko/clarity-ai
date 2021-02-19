package periodicreports

import log.Duration
import log.Host
import log.LogLine
import log.Timestamp

typealias LogLineAction = (LogLine) -> Unit

class PeriodicReportGenerator(
    private val host: Host,
    initialTimestamp: Timestamp,
    private val reportPeriod: Duration,
    private val maxTolerableLag: Duration
) {
    private var nextReportTimestamp = initialTimestamp + reportPeriod
    private var timestampHighWatermark = initialTimestamp

    fun processLogLine(line: LogLine, collector: ReportCollector, actionsByFilters: Map<LogLineFilter, LogLineAction>) {
        val (timestamp, _, _) = line
        if (timestamp >= nextReportTimestamp) {
            collector.emitReport()
            val periodsWithNoLines = (timestamp / nextReportTimestamp) - 1
            if (periodsWithNoLines > 0) {
                collector.emitEmptyReports(periodsWithNoLines)
            }
            nextReportTimestamp += reportPeriod
        }
        if (timestamp >= timestampHighWatermark - maxTolerableLag) {
            actionsByFilters.forEach { (filter, action) -> if (filter(line)) action(line) }
        }
        if (timestamp > timestampHighWatermark) {
            timestampHighWatermark = timestamp
        }
    }
}
