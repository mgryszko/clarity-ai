package memory

import log.LogLine
import log.LogReader
import log.Timestamp

class ListLogReader(private val lines: List<LogLine>) : LogReader {
    override suspend fun readLines(action: (LogLine) -> Unit) = lines.forEach(action)

    override fun getInitialTimestamp(): Timestamp = lines.first().timestamp
}
