import log.LogLine
import log.LogReader

class CollectionLogReader(private val lines: List<LogLine>) : LogReader {
    override fun readLines(action: (LogLine) -> Unit) = lines.forEach(action)

    override fun firstLine(): LogLine = lines.first()
}
