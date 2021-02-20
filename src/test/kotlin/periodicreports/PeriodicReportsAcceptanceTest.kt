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
        actionsByFilters = mapOf(
            incomingConnectionToTarget(Host("Aaliayh")) to onOutgoingConnection(emitter),
            outgoingConnectionFromSource(Host("Shaian")) to onIncomingConnection(emitter),
        )
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
            ConnectionReport(timestamp = Timestamp(1565650804351)),
            ConnectionReport(timestamp = Timestamp(1565654404351), incomingFrom = setOf(Host("Dayonte"))),
            ConnectionReport(timestamp = Timestamp(1565658004351), incomingFrom = setOf(Host("Shaquera"))),
            ConnectionReport(timestamp = Timestamp(1565661604351)),
            ConnectionReport(timestamp = Timestamp(1565665204351), outgoingTo = setOf(Host("Prue"))),
            ConnectionReport(timestamp = Timestamp(1565668804351)),
            ConnectionReport(timestamp = Timestamp(1565672404351), incomingFrom = setOf(Host("Zidan"))),
            ConnectionReport(timestamp = Timestamp(1565676004351)),
            ConnectionReport(timestamp = Timestamp(1565679604351)),
            ConnectionReport(timestamp = Timestamp(1565683204351), incomingFrom = setOf(Host("Adalhi"), Host("Terryn"))),
            ConnectionReport(timestamp = Timestamp(1565686804351), incomingFrom = setOf(Host("Kyus"))),
            ConnectionReport(timestamp = Timestamp(1565690404351), incomingFrom = setOf(Host("Taison"))),
            ConnectionReport(timestamp = Timestamp(1565694004351)),
            ConnectionReport(timestamp = Timestamp(1565697604351), outgoingTo = setOf(Host("Heshy"))),
            ConnectionReport(timestamp = Timestamp(1565701204351), incomingFrom = setOf(Host("Cliff"))),
            ConnectionReport(timestamp = Timestamp(1565704804351), outgoingTo = setOf(Host("Maryanne"))),
            ConnectionReport(timestamp = Timestamp(1565708404351)),
            ConnectionReport(timestamp = Timestamp(1565712004351)),
            ConnectionReport(timestamp = Timestamp(1565715604351)),
            ConnectionReport(timestamp = Timestamp(1565719204351), incomingFrom = setOf(Host("Ivy"))),
            ConnectionReport(timestamp = Timestamp(1565722804351), outgoingTo = setOf(Host("Rownan"))),
            ConnectionReport(timestamp = Timestamp(1565726404351), incomingFrom = setOf(Host("Azarel")), outgoingTo = setOf(Host("Kesler"))),
            ConnectionReport(timestamp = Timestamp(1565730004351)),
            ConnectionReport(timestamp = Timestamp(1565733604351)),
        )
    }
}