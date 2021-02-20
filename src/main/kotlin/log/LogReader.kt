package log

interface LogReader {
    suspend fun readLines(action: (LogLine) -> Unit)
}