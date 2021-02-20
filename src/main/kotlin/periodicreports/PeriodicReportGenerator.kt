package periodicreports

import log.Duration
import log.LogLine
import log.Timestamp

class PeriodicReportGenerator(
    private val emitter: ReportEmitter,
    private val actionsByFilters: Map<LogLineFilter, LogLineAction>,
    initialTimestamp: Timestamp,
    private val reportPeriod: Duration,
    private val maxTolerableLag: Duration
) {
    private var nextReportTimestamp = initialTimestamp + reportPeriod
    private var timestampHighWatermark = initialTimestamp

    fun processLogLine(line: LogLine) {
        val (timestamp, _, _) = line
        if (timestamp >= nextReportTimestamp) {
            emitter.emitReport(nextReportTimestamp)
            emitReportsForGaps(timestamp)
            nextReportTimestamp += reportPeriod
        }
        if (timestamp >= timestampHighWatermark - maxTolerableLag) {
            actionsByFilters.forEach { (filter, action) -> if (filter(line)) action(line) }
        }
        if (timestamp > timestampHighWatermark) {
            timestampHighWatermark = timestamp
        }
    }

    private fun emitReportsForGaps(timestamp: Timestamp) {
        val periodsWithNoLines = (timestamp / nextReportTimestamp) - 1
        repeat(periodsWithNoLines.toInt()) {
            emitter.emitEmptyReport()
            nextReportTimestamp += reportPeriod
        }
    }

    fun noMoreLines() {
        emitter.emitReport(nextReportTimestamp)
    }
}
