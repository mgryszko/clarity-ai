import java.io.File

fun main(args : Array<String>) {
    val log = File(args[0]).readText()
    val connectedTo = args[1]
    val from = args[2].toLong()
    val to = args[3].toLong()

    val hosts = parse(log = log, connectedTo = connectedTo, from = from, to = to)

    println(hosts)
}

fun parse(log: String, connectedTo: String, from: Long, to: Long): Set<String> =
    parse(log.lines().asSequence(), connectedTo, from, to)

fun parse(
    lines: Sequence<String>,
    connectedTo: String,
    from: Long,
    to: Long
) = lines
    .filter {
        val (timestamp, _, host) = it.split(" ")
        host == connectedTo && timestamp.toLong() >= from && timestamp.toLong() <= to
    }
    .map { it.split(" ")[1] }
    .toSet()
