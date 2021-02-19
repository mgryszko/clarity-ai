package file

import kotlinx.coroutines.delay
import log.Duration
import log.Host
import log.LogLine
import log.LogReader
import log.Timestamp
import java.io.File

class FileLogReader(
    private val logFileName: String,
    timeout: Duration,
    private val pollingInterval: Duration = Duration(100)
) : LogReader {
    private val polls = timeout.ms / pollingInterval.ms

    override suspend fun readLines(action: (LogLine) -> Unit) {
        File(logFileName).bufferedReader().use { reader ->
            var remainingPolls = polls
            do {
                reader.lineSequence().forEach {
                    action(parse(it))
                    remainingPolls = polls
                }
                if (remainingPolls > 0) {
                    delay(pollingInterval.ms)
                }
                remainingPolls--
            } while (remainingPolls > 0)
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