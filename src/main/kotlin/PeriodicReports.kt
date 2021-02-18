fun main(args: Array<String>) {
    val logFileName = args[0]
    val host = args[1]
    val reportPeriodMs = args[2].toLong()
    val maxTolerableLagMs = args[3].toLong()

    handlePeriodicReports(ReportCollector(::println), FileLogReader(logFileName), host, reportPeriodMs, maxTolerableLagMs)
}

fun handlePeriodicReports(
    collector: ReportCollector,
    logReader: FileLogReader,
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

fun generatePeriodicReports(
    lines: Sequence<LogLine>,
    reportCollector: ReportCollector,
    reportGenerator: PeriodicReportGenerator,
) {
    lines.forEach { line -> reportGenerator.processLogLine(line, reportCollector) }
    reportCollector.closeReport()
}
