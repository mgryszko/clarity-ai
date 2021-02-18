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
    val reportGenerator = PeriodicReportGenerator(
        Host(host),
        firstLine(logFileName).timestamp,
        Duration(reportPeriodMs),
        Duration(maxTolerableLagMs)
    )
    File(logFileName).bufferedReader().use { reader ->
        var done = false
        while (!done) {
            val line = reader.readLine()
            if (line != null) {
                reportGenerator.processLogLine(parse(line), collector)
            } else {
                done = true
            }
        }
        collector.closeReport()
    }
}

private fun firstLine(logFileName: String): LogLine {
    val line = File(logFileName).useLines { it.first() }
    return parse(line)
}

fun generatePeriodicReports(
    lines: Sequence<LogLine>,
    reportCollector: ReportCollector,
    reportGenerator: PeriodicReportGenerator,
) {
    lines.forEach { line -> reportGenerator.processLogLine(line, reportCollector) }
    reportCollector.closeReport()
}
