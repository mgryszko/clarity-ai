package file

import kotlinx.coroutines.delay
import log.Duration
import log.Host
import log.LogLine
import log.LogReader
import log.Timestamp
import java.io.File

class UnparseableLogLineException(line: String, cause: Throwable) : RuntimeException("Line $line not parseable", cause)

class LogEmptyException : RuntimeException("Log is empty")

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
                    runCatching {
                        parse(line)?.let { action(it) }
                    }.onFailure { e -> throw UnparseableLogLineException(line, e) }

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
            it.filter(String::isNotBlank).firstOrNull()
        }
        return line?.let { parse(it)?.timestamp } ?: throw LogEmptyException()
    }

    private fun parse(line: String): LogLine? =
        if (line.isNotBlank()) {
            val (timestamp, source, target) = line.split(" ")
            LogLine(timestamp = Timestamp(timestamp.toLong()), source = Host(source), target = Host(target))
        } else {
            null
        }
}