package connectedsources

import ch.tutteli.atrium.api.fluent.en_GB.containsExactly
import ch.tutteli.atrium.api.verbs.expect
import file.FileLogReader
import log.Host
import log.Timestamp
import java.io.File
import kotlin.test.Test

class ConnectedSourceHostsAcceptanceTest {
    val logFileName = javaClass.getResource("/input-file-10000.txt").path!!
    val handler = ConnectedSourceHostsHandler(FileLogReader(File(logFileName)))

    @Test
    fun `log from file, spying reports observer`() {
        val hosts = handler.handle(
            target = Host("Aaliayh"),
            from = Timestamp(1565656607767),
            to = Timestamp(1565680778409)
        )

        expect(hosts).containsExactly(
            Host("Shaquera"),
            Host("Zidan"),
            Host("Adalhi"),
        )
    }
}