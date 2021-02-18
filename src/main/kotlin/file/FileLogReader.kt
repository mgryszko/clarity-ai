package file

import log.Host
import log.LogLine
import log.LogReader
import log.Timestamp
import java.io.File

class FileLogReader(private val logFileName: String) : LogReader {
    override fun readLines(action: (LogLine) -> Unit) {
        File(logFileName).bufferedReader().use { reader ->
            var line: String?
            do {
                line = reader.readLine()
                line?.let { action(parse(it)) }
            } while (line != null)
        }
    }

    override fun firstLine(): LogLine {
        val line = File(logFileName).useLines { it.first() }
        return parse(line)
    }

    private fun parse(line: String): LogLine {
        val (timestamp, source, target) = line.split(" ")
        return LogLine(timestamp = Timestamp(timestamp.toLong()), source = Host(source), target = Host(target))
    }
}