package connectedsources

import file.FileLogReader
import kotlinx.coroutines.runBlocking
import log.Duration
import log.Host
import log.LogLine
import log.Timestamp
import java.io.File

class ConnectedSourceHostsHandler {
    fun handle(logFileName: String, target: String, fromMs: Long, toMs: Long, onSourceHosts: (Set<Host>) -> Unit) {
        val logLines = mutableListOf<LogLine>()
        runBlocking {
            FileLogReader(File(logFileName), Duration(0)).readLines { line -> logLines.add(line) }
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