package periodicreports

import ch.tutteli.atrium.api.fluent.en_GB.containsExactly
import ch.tutteli.atrium.api.verbs.expect
import file.FileLogReader
import log.Duration
import log.Host
import log.Timestamp
import java.io.File
import kotlin.test.Test

class PeriodicReportsAcceptanceTest {
    val renderer = CollectingReportRenderer()
    val emitter = ReportCollector(renderer)
    val logFile = File(javaClass.getResource("/input-file-10000.txt").path!!)
    val handler = PeriodicReportsHandler(
        logReader = FileLogReader(logFile, Duration(0)),
        emitter = emitter,
        actionsByFilters = mapOf(incomingConnectionToTarget(Host("Aaliayh")) to onOutgoingConnection(emitter))
    )
    val oneHour = Duration(60 * 60 * 1000)
    val fiveMinutes = Duration(5 * 60 * 1000)

    @Test
    fun handle() {
        handler.handle(
            reportPeriod = oneHour,
            maxTolerableLag = fiveMinutes
        )

        expect(renderer.reports).containsExactly(
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565650804351)),
            ConnectionReport(incomingFrom = setOf(Host("Dayonte")), timestamp = Timestamp(1565654404351)),
            ConnectionReport(incomingFrom = setOf(Host("Shaquera")), timestamp = Timestamp(1565658004351)),
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565661604351)),
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565665204351)),
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565668804351)),
            ConnectionReport(incomingFrom = setOf(Host("Zidan")), timestamp = Timestamp(1565672404351)),
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565676004351)),
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565679604351)),
            ConnectionReport(incomingFrom = setOf(Host("Adalhi"), Host("Terryn")), timestamp = Timestamp(1565683204351)),
            ConnectionReport(incomingFrom = setOf(Host("Kyus")), timestamp = Timestamp(1565686804351)),
            ConnectionReport(incomingFrom = setOf(Host("Taison")), timestamp = Timestamp(1565690404351)),
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565694004351)),
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565697604351)),
            ConnectionReport(incomingFrom = setOf(Host("Cliff")), timestamp = Timestamp(1565701204351)),
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565704804351)),
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565708404351)),
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565712004351)),
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565715604351)),
            ConnectionReport(incomingFrom = setOf(Host("Ivy")), timestamp = Timestamp(1565719204351)),
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565722804351)),
            ConnectionReport(incomingFrom = setOf(Host("Azarel")), timestamp = Timestamp(1565726404351)),
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565730004351)),
            ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1565733604351)),
        )
    }
}