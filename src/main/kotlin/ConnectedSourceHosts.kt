import java.io.File

fun main(args: Array<String>) {
    val logFileName = args[0]
    val target = args[1]
    val from = args[2].toLong()
    val to = args[3].toLong()

    readFindPrint(logFileName, target, from, to, ::println)
}

fun readFindPrint(logFileName: String, target: String, from: Long, to: Long, onSourceHosts: (Collection<String>) -> Unit) {
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
    lines.filter(String::isNotBlank).map(::parse)

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
