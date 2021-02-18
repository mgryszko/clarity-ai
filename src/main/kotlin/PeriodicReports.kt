import java.io.File

fun main(args: Array<String>) {
    val logFileName = args[0]
    val host = args[1]
    val reportPeriodMs = args[2].toLong()
    val maxTolerableLagMs = args[3].toLong()

    handlePeriodicReports(logFileName, host, reportPeriodMs, maxTolerableLagMs, ::println)
}

fun handlePeriodicReports(
    logFileName: String,
    host: String,
    reportPeriodMs: Long,
    maxTolerableLagMs: Long,
    onReports: (List<Set<Host>>) -> Unit
) {
    val collector = ReportCollector()
    val reports = read(logFileName) { lines ->
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

    onReports(collector.reports)
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
): List<Set<Host>> {
    val hosts = mutableSetOf<Host>()
    val reports = mutableListOf<Set<Host>>()
    var nextReportTimestamp = initialTimestamp + reportPeriod
    var timestampHighWatermark = initialTimestamp
    lines.forEach { (timestamp, source, target) ->
        if (timestamp >= nextReportTimestamp) {
            reports.add(hosts.toSet())
            hosts.clear()
            reportCollector.closeReport()
            if (timestamp / nextReportTimestamp > 1) {
                repeat((timestamp / nextReportTimestamp).toInt() - 1) {
                    reports.add(emptySet())
                    reportCollector.closeReport()
                }
            }
            nextReportTimestamp += reportPeriod
        }
        if (timestamp >= timestampHighWatermark - maxTolerableLag) {
            if (target == host) {
                hosts.add(source)
                reportCollector.sourceHostConnected(source)
            }
        }
        if (timestamp > timestampHighWatermark) {
            timestampHighWatermark = timestamp
        }
    }
    reports.add(hosts.toSet())
    reportCollector.closeReport()
    return reports
}

