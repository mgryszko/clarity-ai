package file

import kotlinx.coroutines.delay
import log.Duration
import log.Host
import log.LogLine
import log.LogReader
import log.Timestamp
import java.io.File
import java.lang.IllegalStateException

class FileLogReader(
    private val logFile: File,
    timeout: Duration,
    private val pollingInterval: Duration = Duration(100)
) : LogReader {
    private val polls = timeout.ms / pollingInterval.ms

    override suspend fun readLines(action: (LogLine) -> Unit) {
        logFile.bufferedReader().use { reader ->
            var remainingPolls = polls
            do {
                reader.lineSequence().forEach { line ->
                    parse(line)?.let { action(it) }
                    remainingPolls = polls
                }
                if (remainingPolls > 0) {
                    delay(pollingInterval.ms)
                }
                remainingPolls--
            } while (remainingPolls > 0)
        }
    }

    override fun getInitialTimestamp(): Timestamp {
        val line = logFile.useLines {
            it.filter(String::isNotBlank).first()
        }
        return parse(line)?.timestamp ?: throw IllegalStateException("Log file must contain at least one parseable line")
    }

    private fun parse(line: String): LogLine? =
        if (line.isNotBlank()) {
            val (timestamp, source, target) = line.split(" ")
            LogLine(timestamp = Timestamp(timestamp.toLong()), source = Host(source), target = Host(target))
        } else {
            null
        }
}