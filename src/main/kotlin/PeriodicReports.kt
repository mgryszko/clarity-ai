fun main(args: Array<String>) {
    val logFileName = args[0]
    val host = args[1]
    val reportPeriodMs = args[2].toLong()
    val maxTolerableLagMs = args[3].toLong()

    PeriodicReportsHandler(FileLogReader(logFileName), ReportCollector(::println)).handle(host, reportPeriodMs, maxTolerableLagMs)
}

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
