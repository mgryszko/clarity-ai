import java.io.File

fun main(args : Array<String>) {
    val logFileName = args[0]
    val connectedTo = args[1]
    val from = args[2].toLong()
    val to = args[3].toLong()

    readParsePrint(logFileName, connectedTo, from, to, ::println)
}

fun readParsePrint(logFileName: String, connectedTo: String, from: Long, to: Long, onConnectedHosts: (Collection<String>) -> Unit) {
    val hosts = File(logFileName).useLines {
        parse(lines = it, connectedTo = Host(connectedTo), from = Timestamp(from), to = Timestamp(to))
    }

    onConnectedHosts(hosts.map(Host::name))
}

inline class Host(val name: String)
inline class Timestamp(val instant: Long)

fun parse(
    lines: Sequence<String>,
    connectedTo: Host,
    from: Timestamp,
    to: Timestamp
): Set<Host> = lines
    .filter {
        val (timestamp, _, host) = it.split(" ")
        host == connectedTo.name && timestamp.toLong() >= from.instant && timestamp.toLong() <= to.instant
    }
    .map { Host(it.split(" ")[1]) }
    .toSet()
