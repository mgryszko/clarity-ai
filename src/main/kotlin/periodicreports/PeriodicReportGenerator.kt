package periodicreports

import log.Duration
import log.Host
import log.LogLine
import log.Timestamp

class PeriodicReportGenerator(
    private val host: Host,
    initialTimestamp: Timestamp,
    private val reportPeriod: Duration,
    private val maxTolerableLag: Duration
) {
    private var nextReportTimestamp = initialTimestamp + reportPeriod
    private var timestampHighWatermark = initialTimestamp

    fun processLogLine(line: LogLine, collector: ReportCollector) {
        val (timestamp, source, target) = line
        if (timestamp >= nextReportTimestamp) {
            collector.closeReport()
            val periodsWithNoLines = (timestamp / nextReportTimestamp) - 1
            if (periodsWithNoLines > 0) {
                collector.closeEmptyReports(periodsWithNoLines)
            }
            nextReportTimestamp += reportPeriod
        }
        if (timestamp >= timestampHighWatermark - maxTolerableLag) {
            if (target == host) {
                collector.sourceHostConnected(source)
            }
        }
        if (timestamp > timestampHighWatermark) {
            timestampHighWatermark = timestamp
        }
    }
}
