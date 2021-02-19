package file

import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import log.Duration
import log.Timestamp
import org.junit.jupiter.api.Nested
import java.io.File
import kotlin.test.Test

@ExperimentalCoroutinesApi
class FileLogReaderTest {
    @Nested
    inner class ReadLines {
        @Test
        fun `with timeout`() = runBlockingTest {
            val reader = FileLogReader(100.linesLogFile(), Duration(500), Duration(100))

            val linesRead = countReadLines(reader)

            expect(linesRead).toBe(100)
            expect(currentTime).toBe(500)
        }

        @Test
        fun `zero timeout`() = runBlockingTest {
            val reader = FileLogReader(1.linesLogFile(), Duration(0))

            val linesRead = countReadLines(reader)

            expect(linesRead).toBe(1)
            expect(currentTime).toBe(0)
        }

        @Test
        fun `empty file`() = runBlockingTest {
            val reader = FileLogReader(0.linesLogFile(), Duration(0))

            expect(countReadLines(reader)).toBe(0)
        }

        suspend fun countReadLines(reader: FileLogReader): Int {
            var linesRead = 0
            reader.readLines { linesRead++ }
            return linesRead
        }
    }

    @Nested
    inner class GetFirstTimestamp {
        @Test
        fun get() = runBlockingTest {
            val reader = FileLogReader(1.linesLogFile(), Duration(0))

            expect(reader.getInitialTimestamp()).toBe(Timestamp(0))
        }
    }

    fun Int.linesLogFile() = File.createTempFile("log", ".txt").let { file ->
        file.deleteOnExit()
        file.printWriter().use { writer ->
            repeat(this) { timestamp ->
                writer.println("$timestamp source target")
            }
        }
        file
    }
}