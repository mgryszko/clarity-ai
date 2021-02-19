package file

import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import log.Duration
import kotlin.test.Test

@ExperimentalCoroutinesApi
class FileLogReaderTest {
    @Test
    fun `with timeout`() = runBlockingTest {
        val logFileName = javaClass.getResource("/input-file-10000.txt").path
        val reader = FileLogReader(logFileName, Duration(500), Duration(100))
        var linesRead = 0

        reader.readLines { linesRead++ }

        expect(linesRead).toBe(10000)
        expect(currentTime).toBe(500)
    }

    @Test
    fun `zero timeout`() = runBlockingTest {
        val logFileName = javaClass.getResource("/input-file-10000.txt").path
        val reader = FileLogReader(logFileName, Duration(0))
        var linesRead = 0

        reader.readLines { linesRead++ }

        expect(linesRead).toBe(10000)
        expect(currentTime).toBe(0)
    }
}