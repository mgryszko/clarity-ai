package file

import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import log.Duration
import java.io.File
import kotlin.test.Test

@ExperimentalCoroutinesApi
class FileLogReaderTest {
    @Test
    fun `with timeout`() = runBlockingTest {
        val reader = FileLogReader(100.linesLogFile(), Duration(500), Duration(100))
        var linesRead = 0

        reader.readLines { linesRead++ }

        expect(linesRead).toBe(100)
        expect(currentTime).toBe(500)
    }

    @Test
    fun `zero timeout`() = runBlockingTest {
        val reader = FileLogReader(1.linesLogFile(), Duration(0))
        var linesRead = 0

        reader.readLines { linesRead++ }

        expect(linesRead).toBe(10000)
        expect(currentTime).toBe(0)
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