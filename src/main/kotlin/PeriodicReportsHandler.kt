class PeriodicReportsHandler(private val logReader: LogReader, private val collector: ReportCollector) {
    fun handle(
        host: String,
        reportPeriodMs: Long,
        maxTolerableLagMs: Long,
    ) {
        val reportGenerator = PeriodicReportGenerator(
            Host(host),
            logReader.firstLine().timestamp,
            Duration(reportPeriodMs),
            Duration(maxTolerableLagMs)
        )
        logReader.readLines { line ->
            reportGenerator.processLogLine(line, collector)
        }
        collector.closeReport()
    }
}