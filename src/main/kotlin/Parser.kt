import java.io.File

fun main(args : Array<String>) {
    val logFileName = args[0]
    val connectedTo = args[1]
    val from = args[2].toLong()
    val to = args[3].toLong()

    val hosts = File(logFileName).useLines {
        parse(lines = it, connectedTo = connectedTo, from = from, to = to)
    }

    println(hosts)
}

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
