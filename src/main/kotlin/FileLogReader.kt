import java.io.File

class FileLogReader(private val logFileName: String) {
    fun readLines(action: (LogLine) -> Unit) {
        File(logFileName).bufferedReader().use { reader ->
            var line: String?
            do {
                line = reader.readLine()
                line?.let { action(parse(it)) }
            } while (line != null)
        }
    }

    fun firstLine(): LogLine {
        val line = File(logFileName).useLines { it.first() }
        return parse(line)
    }
}