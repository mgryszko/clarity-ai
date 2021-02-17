import java.io.File

fun main(args: Array<String>) {
    val logFileName = args[0]
    val host = args[1]
    val reportWindowMs = args[2].toLong()
    val maxTolerableLagMs = args[3].toLong()

    handleWindowedReport(logFileName, host, reportWindowMs, maxTolerableLagMs, ::println)
}

fun handleWindowedReport(
    logFileName: String,
    host: String,
    reportWindowMs: Long,
    maxTolerableLagMs: Long,
    onReports: (List<List<String>>) -> Unit
) {
    val reports = read(logFileName) { lines ->
        val parsedLines = parse(lines)
        connectedSourceHosts(
            lines = parsedLines,
            target = Host(host),
            initialTimestamp = firstLine(logFileName).timestamp,
            reportWindow = Duration(reportWindowMs),
            maxTolerableLag = Duration(maxTolerableLagMs),
        )
    }

    onReports(reports.map { it.map(Host::name) })
}

fun firstLine(logFileName: String): LogLine {
    val line = File(logFileName).useLines { it.first() }
    return parse(line)
}

private fun <T> read(fileName: String, processLines: (Sequence<String>) -> T): T =
    File(fileName).useLines(Charsets.UTF_8, processLines)

private fun parse(lines: Sequence<String>): Sequence<LogLine> =
    lines.filter(String::isNotBlank).map(::parse)

private fun parse(line: String): LogLine {
    val (timestamp, source, target) = line.split(" ")
    return LogLine(timestamp = Timestamp(timestamp.toLong()), source = Host(source), target = Host(target))
}

fun connectedSourceHosts(
    lines: Sequence<LogLine>,
    target: Host,
    initialTimestamp: Timestamp,
    reportWindow: Duration,
    maxTolerableLag: Duration,
): List<Set<Host>> {
    val hosts = mutableSetOf<Host>()
    val reports = mutableListOf<Set<Host>>()
    var nextWindow = initialTimestamp + reportWindow
    var timestampHighWatermark = initialTimestamp
    lines.forEach { (timestamp, source, lineTarget) ->
        if (timestamp >= nextWindow) {
            reports.add(hosts.toSet())
            hosts.clear()
            if (timestamp / nextWindow > 1) {
                repeat((timestamp / nextWindow).toInt() - 1) {
                    reports.add(emptySet())
                }
            }
            nextWindow += reportWindow
        }
        if (timestamp >= timestampHighWatermark - maxTolerableLag) {
            if (lineTarget == target) {
                hosts.add(source)
            }
        }
        if (timestamp > timestampHighWatermark) {
            timestampHighWatermark = timestamp
        }
    }
    reports.add(hosts.toSet())
    return reports
}

