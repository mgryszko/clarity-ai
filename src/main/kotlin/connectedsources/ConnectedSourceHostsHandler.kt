package connectedsources

import log.Host
import log.LogLine
import log.Timestamp
import java.io.File

class ConnectedSourceHostsHandler {
    fun handle(logFileName: String, target: String, fromMs: Long, toMs: Long, onSourceHosts: (Set<Host>) -> Unit) {
        val hosts = read(logFileName) { lines ->
            val parsedLines = parse(lines)
            findSourceHosts(
                lines = parsedLines,
                target = Host(target),
                from = Timestamp(fromMs),
                to = Timestamp(toMs)
            )
        }

        onSourceHosts(hosts)
    }

    private fun <T> read(fileName: String, processLines: (Sequence<String>) -> T): T =
        File(fileName).useLines(Charsets.UTF_8, processLines)

    private fun parse(lines: Sequence<String>): Sequence<LogLine> =
        lines.filter(String::isNotBlank).map(::parse)

    private fun parse(line: String): LogLine {
        val (timestamp, source, target) = line.split(" ")
        return LogLine(timestamp = Timestamp(timestamp.toLong()), source = Host(source), target = Host(target))
    }
}

fun findSourceHosts(
    lines: Sequence<LogLine>,
    target: Host,
    from: Timestamp,
    to: Timestamp
): Set<Host> = lines
    .filter { (timestamp, _, lineTarget) ->
        lineTarget == target && timestamp >= from && timestamp <= to
    }
    .map(LogLine::source)
    .toSet()