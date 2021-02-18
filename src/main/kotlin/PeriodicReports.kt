import java.io.File

fun main(args: Array<String>) {
    val logFileName = args[0]
    val host = args[1]
    val reportPeriodMs = args[2].toLong()
    val maxTolerableLagMs = args[3].toLong()

    handlePeriodicReports(ReportCollector(::println), logFileName, host, reportPeriodMs, maxTolerableLagMs)
}

fun handlePeriodicReports(
    collector: ReportCollector,
    logFileName: String,
    host: String,
    reportPeriodMs: Long,
    maxTolerableLagMs: Long,
) {
    read(logFileName) { lines ->
        val parsedLines = parse(lines)
        generatePeriodicReports(
            lines = parsedLines,
            host = Host(host),
            initialTimestamp = firstLine(logFileName).timestamp,
            reportPeriod = Duration(reportPeriodMs),
            maxTolerableLag = Duration(maxTolerableLagMs),
            reportCollector = collector
        )
    }
}

private fun <T> read(fileName: String, processLines: (Sequence<String>) -> T): T =
    File(fileName).useLines(Charsets.UTF_8, processLines)

private fun firstLine(logFileName: String): LogLine {
    val line = File(logFileName).useLines { it.first() }
    return parse(line)
}

private fun parse(lines: Sequence<String>): Sequence<LogLine> =
    lines.filter(String::isNotBlank).map(::parse)

fun generatePeriodicReports(
    lines: Sequence<LogLine>,
    host: Host,
    initialTimestamp: Timestamp,
    reportPeriod: Duration,
    maxTolerableLag: Duration,
    reportCollector: ReportCollector,
) {
    val reportGenerator = PeriodicReportGenerator(host, initialTimestamp, reportPeriod, maxTolerableLag)
    lines.forEach { line -> reportGenerator.processLogLine(line, reportCollector) }
    reportCollector.closeReport()
}
