package periodicreports

import ch.tutteli.atrium.api.fluent.en_GB.contains
import ch.tutteli.atrium.api.fluent.en_GB.containsExactly
import ch.tutteli.atrium.api.fluent.en_GB.feature
import ch.tutteli.atrium.api.fluent.en_GB.hasSize
import ch.tutteli.atrium.api.fluent.en_GB.inAnyOrder
import ch.tutteli.atrium.api.fluent.en_GB.toBe
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
            pass to topOutgoingConnections(emitter),
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

        val reports = renderer.reports

        expect(reports).hasSize(24)
        expect(reports[0]).toBe(
            ConnectionReport(
                timestamp = Timestamp(1565650804351),
                topOutgoing = setOf(
                    Host("Keimy"),
                    Host("Kishauna"),
                    Host("Eveliz"),
                    Host("Traysen"),
                    Host("Lachandra"),
                    Host("Santangelo"),
                    Host("Donnis")
                ),
                topOutgoingNumber = 2
            )
        )
        expect(reports[14]).toBe(
            ConnectionReport(
                timestamp = Timestamp(1565701204351),
                incomingFrom = setOf(Host("Cliff")),
                topOutgoing = setOf(Host("Kamyree")),
                topOutgoingNumber = 3
            )
        )
        expect(reports[23]).toBe(
            ConnectionReport(
                timestamp = Timestamp(1565733604351),
                topOutgoing = setOf(
                    Host("Burnett"),
                    Host("Ishi"),
                    Host("Saphyra"),
                    Host("Kingsly"),
                    Host("Kiaeem"),
                    Host("Kahleil"),
                    Host("Gaetan"),
                    Host("Katrinna"),
                    Host("Jaiceyon"),
                    Host("Dashyra")
                ),
                topOutgoingNumber = 2
            )
        )
    }
}