package file

import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.fluent.en_GB.toThrow
import ch.tutteli.atrium.api.verbs.expect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import log.Duration
import java.io.File
import kotlin.test.Test

@ExperimentalCoroutinesApi
class FileLogReaderTest {
    private val blank = " \t  "

    @Test
    fun `with timeout`() = runBlockingTest {
        val reader = FileLogReader(100.linesLogFile(), Duration(500), Duration(100))

        val linesRead = countReadLines(reader)

        expect(linesRead).toBe(100)
        expect(currentTime).toBe(500)
    }

    @Test
    fun `zero timeout`() = runBlockingTest {
        val reader = FileLogReader(1.linesLogFile())

        val linesRead = countReadLines(reader)

        expect(linesRead).toBe(1)
        expect(currentTime).toBe(0)
    }

    @Test
    fun `empty file`() = runBlockingTest {
        val reader = FileLogReader(0.linesLogFile())

        expect(countReadLines(reader)).toBe(0)
    }

    @Test
    fun `blank lines`() = runBlockingTest {
        val file = tempFile { file ->
            file.printWriter().use { writer ->
                writer.println()
                writer.println("1 :: ::")
                writer.println()
                writer.println("1 :: ::")
                writer.println(blank)
                writer.println()
            }
        }
        val reader = FileLogReader(file)

        expect(countReadLines(reader)).toBe(2)
    }

    @Test
    fun `wrong format`() {
        expect {
            runBlockingTest {
                val reader = FileLogReader(tempFile { it.writeText("1 source") })
                countReadLines(reader)
            }
        }.toThrow<UnparseableLogLineException>()
    }

    suspend fun countReadLines(reader: FileLogReader): Int {
        var linesRead = 0
        reader.readLines { linesRead++ }
        return linesRead
    }

    fun Int.linesLogFile() = tempFile { file ->
        file.printWriter().use { writer ->
            repeat(this) { timestamp ->
                writer.println("$timestamp source target")
            }
        }
    }

    fun tempFile(block: (File) -> Unit): File =
        File.createTempFile("log", ".txt").let { file ->
            file.deleteOnExit()
            block(file)
            file
        }
}