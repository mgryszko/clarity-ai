package connectedsources

import kotlinx.coroutines.runBlocking
import log.Host
import log.LogLine
import log.LogReader
import log.Timestamp

class ConnectedSourceHostsHandler(private val logReader: LogReader) {
    fun handle(target: String, fromMs: Long, toMs: Long, onSourceHosts: (Set<Host>) -> Unit) {
        val logLines = mutableListOf<LogLine>()
        runBlocking {
            logReader.readLines { line -> logLines.add(line) }
        }
        val hosts = findSourceHosts(
            lines = logLines.asSequence(),
            target = Host(target),
            from = Timestamp(fromMs),
            to = Timestamp(toMs)
        )

        onSourceHosts(hosts)
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