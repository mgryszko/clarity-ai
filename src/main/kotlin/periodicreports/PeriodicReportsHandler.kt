package periodicreports

import log.Duration
import log.Host
import log.LogReader

class PeriodicReportsHandler(private val logReader: LogReader, private val collector: ReportCollector) {
    fun handle(host: Host, reportPeriod: Duration, maxTolerableLag: Duration) {
        val reportGenerator = PeriodicReportGenerator(
            host = host,
            initialTimestamp = logReader.firstLine().timestamp,
            reportPeriod = reportPeriod,
            maxTolerableLag = maxTolerableLag
        )
        logReader.readLines { line ->
            reportGenerator.processLogLine(line, collector)
        }
        collector.closeReport()
    }
}