import java.io.File

fun main(args: Array<String>) {
    val logFileName = args[0]
    val connectedTo = args[1]
    val from = args[2].toLong()
    val to = args[3].toLong()

    readParsePrint(logFileName, connectedTo, from, to, ::println)
}

fun readParsePrint(logFileName: String, connectedTo: String, from: Long, to: Long, onConnectedHosts: (Collection<String>) -> Unit) {
    val hosts = read(logFileName) { lines ->
        val parsedLines = parse(lines)
        findConnectedHosts(
            lines = parsedLines,
            connectedTo = Host(connectedTo),
            from = Timestamp(from),
            to = Timestamp(to)
        )
    }

    onConnectedHosts(hosts.map(Host::name))
}

private fun <T> read(fileName: String, processLines: (Sequence<String>) -> T): T =
    File(fileName).useLines(Charsets.UTF_8, processLines)

private fun parse(lines: Sequence<String>): Sequence<LogLine> = lines.map {
    val (timestamp, connectedFrom, connectedTo) = it.split(" ")
    LogLine(timestamp = Timestamp(timestamp.toLong()), connectedFrom = Host(connectedFrom), connectedTo = Host(connectedTo))
}

inline class Host(val name: String)
inline class Timestamp(val instant: Long)
data class LogLine(val timestamp: Timestamp, val connectedFrom: Host, val connectedTo: Host)

fun findConnectedHosts(
    lines: Sequence<LogLine>,
    connectedTo: Host,
    from: Timestamp,
    to: Timestamp
): Set<Host> = lines
    .filter { (timestamp, _, lineConnectedTo) ->
        lineConnectedTo == connectedTo && timestamp.instant >= from.instant && timestamp.instant <= to.instant
    }
    .map(LogLine::connectedFrom)
    .toSet()
