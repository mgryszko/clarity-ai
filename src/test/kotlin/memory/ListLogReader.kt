package memory

import log.LogLine
import log.LogReader

class ListLogReader(private val lines: List<LogLine>) : LogReader {
    override suspend fun readLines(action: (LogLine) -> Unit) = lines.forEach(action)

    override fun firstLine(): LogLine = lines.first()
}
