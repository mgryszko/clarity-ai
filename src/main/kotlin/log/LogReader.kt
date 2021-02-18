package log

interface LogReader {
    fun readLines(action: (LogLine) -> Unit)

    fun firstLine(): LogLine
}