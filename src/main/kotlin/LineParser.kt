fun parse(line: String): LogLine {
    val (timestamp, source, target) = line.split(" ")
    return LogLine(timestamp = Timestamp(timestamp.toLong()), source = Host(source), target = Host(target))
}
