package periodicreports

import log.Duration
import log.LogLine
import log.Timestamp

class PeriodicReportGenerator(
    private val emitter: ReportEmitter,
    private val actionsByFilters: Map<LogLineFilter, LogLineAction>,
    private val reportPeriod: Duration,
    private val maxTolerableLag: Duration
) {
    private var firstLine = true
    private var nextReportTimestamp: Timestamp = Timestamp(0)
    private var timestampHighWatermark: Timestamp = Timestamp(0)

    fun processLogLine(line: LogLine) {
        val (timestamp, _, _) = line
        initializeMarkerTimestampsOnFirstLine(timestamp)
        if (shouldEmitReports(timestamp)) {
            emitter.emitReport(nextReportTimestamp)
            emitReportsForGaps(timestamp)
            advanceNextReportTimestamp()
        }
        if (withinTolerableLag(timestamp)) {
            actionsByFilters.forEach { (filter, action) -> if (filter(line)) action(line) }
        }
        updateTimestampHighWatermark(timestamp)
    }

    private fun initializeMarkerTimestampsOnFirstLine(timestamp: Timestamp) {
        if (firstLine) {
            nextReportTimestamp = timestamp + reportPeriod
            timestampHighWatermark = timestamp
            firstLine = false
        }
    }

    private fun shouldEmitReports(timestamp: Timestamp) = timestamp >= nextReportTimestamp

    private fun withinTolerableLag(timestamp: Timestamp) = timestamp >= timestampHighWatermark - maxTolerableLag

    private fun emitReportsForGaps(timestamp: Timestamp) {
        val periodsWithNoLines = (timestamp / nextReportTimestamp) - 1
        repeat(periodsWithNoLines.toInt()) {
            advanceNextReportTimestamp()
            emitter.emitEmptyReport(nextReportTimestamp)
        }
    }

    private fun advanceNextReportTimestamp() {
        nextReportTimestamp += reportPeriod
    }

    private fun updateTimestampHighWatermark(timestamp: Timestamp) {
        if (timestamp > timestampHighWatermark) {
            timestampHighWatermark = timestamp
        }
    }

    fun noMoreLines() {
        emitter.emitReport(nextReportTimestamp)
    }
}
