import java.io.File

fun main(args: Array<String>) {
    val logFileName = args[0]
    val target = args[1]
    val from = args[2].toLong()
    val to = args[3].toLong()

    readParsePrint(logFileName, target, from, to, ::println)
}

fun readParsePrint(logFileName: String, target: String, from: Long, to: Long, onSourceHosts: (Collection<String>) -> Unit) {
    val hosts = read(logFileName) { lines ->
        val parsedLines = parse(lines)
        findSourceHosts(
            lines = parsedLines,
            target = Host(target),
            from = Timestamp(from),
            to = Timestamp(to)
        )
    }

    onSourceHosts(hosts.map(Host::name))
}

private fun <T> read(fileName: String, processLines: (Sequence<String>) -> T): T =
    File(fileName).useLines(Charsets.UTF_8, processLines)

private fun parse(lines: Sequence<String>): Sequence<LogLine> =
    lines
        .filter(String::isNotBlank)
        .map { line ->
            val (timestamp, source, target) = line.split(" ")
            LogLine(timestamp = Timestamp(timestamp.toLong()), source = Host(source), target = Host(target))
        }

inline class Host(val name: String)
inline class Timestamp(val instant: Long)
data class LogLine(val timestamp: Timestamp, val source: Host, val target: Host)

fun findSourceHosts(
    lines: Sequence<LogLine>,
    target: Host,
    from: Timestamp,
    to: Timestamp
): Set<Host> = lines
    .filter { (timestamp, _, lineTarget) ->
        lineTarget == target && timestamp.instant >= from.instant && timestamp.instant <= to.instant
    }
    .map(LogLine::source)
    .toSet()
