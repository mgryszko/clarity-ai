package periodicreports

import ch.tutteli.atrium.api.fluent.en_GB.containsExactly
import ch.tutteli.atrium.api.verbs.expect
import file.FileLogReader
import log.Duration
import log.Host
import log.LogLine
import log.Timestamp
import memory.ListLogReader
import org.junit.jupiter.api.Nested
import java.io.File
import kotlin.test.Test

class PeriodicReportsHandlerTest {
    val reports = mutableListOf<ConnectionReport>()
    val emitter = ReportCollector(object : ReportRenderer {
        override fun render(report: ConnectionReport) {
            reports.add(report)
        }
    })
    val logFile = File(javaClass.getResource("/input-file-10000.txt").path!!)

    @Nested
    inner class HandleLogFromFile {
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

            expect(reports).containsExactly(
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

    @Nested
    inner class TargetsWithIncomingConnectionsFromSource {
        @Test
        fun `received connections from sources in all report periods`() {
            val actionsByFilters = mapOf(outgoingConnectionFromSource(Host("A")) to onIncomingConnection(emitter))

            PeriodicReportsHandler(ListLogReader(lines), emitter, actionsByFilters).handle(
                reportPeriod = Duration(1000),
                maxTolerableLag = Duration(0),
            )

            expect(reports).containsExactly(
                ConnectionReport(outgoingTo = setOf(Host("eta")), timestamp = Timestamp(1000)),
                ConnectionReport(outgoingTo = setOf(Host("iota"), Host("nu")), timestamp = Timestamp(2000)),
                ConnectionReport(
                    outgoingTo = setOf(Host("tau"), Host("psi"), Host("omega")),
                    timestamp = Timestamp(3000)
                ),
                ConnectionReport(outgoingTo = setOf(Host("as"), Host("buki")), timestamp = Timestamp(4000)),
            )
        }

        val lines = listOf(
            LogLine(Timestamp(0), Host("B"), Host("alpha")),
            LogLine(Timestamp(100), Host("C"), Host("beta")),
            LogLine(Timestamp(200), Host("B"), Host("gamma")),
            LogLine(Timestamp(300), Host("B"), Host("delta")),
            LogLine(Timestamp(400), Host("C"), Host("epsilon")),
            LogLine(Timestamp(700), Host("B"), Host("zeta")),
            LogLine(Timestamp(800), Host("A"), Host("eta")),
            LogLine(Timestamp(900), Host("B"), Host("theta")),

            LogLine(Timestamp(1000), Host("A"), Host("iota")),
            LogLine(Timestamp(1100), Host("B"), Host("kappa")),
            LogLine(Timestamp(1200), Host("C"), Host("lambda")),
            LogLine(Timestamp(1400), Host("B"), Host("mu")),
            LogLine(Timestamp(1500), Host("A"), Host("nu")),
            LogLine(Timestamp(1600), Host("B"), Host("omicron")),
            LogLine(Timestamp(1700), Host("C"), Host("pi")),
            LogLine(Timestamp(1800), Host("C"), Host("rho")),
            LogLine(Timestamp(1900), Host("B"), Host("sigma")),

            LogLine(Timestamp(2000), Host("A"), Host("tau")),
            LogLine(Timestamp(2100), Host("B"), Host("upsilon")),
            LogLine(Timestamp(2300), Host("B"), Host("phi")),
            LogLine(Timestamp(2400), Host("C"), Host("chi")),
            LogLine(Timestamp(2500), Host("A"), Host("psi")),
            LogLine(Timestamp(2900), Host("A"), Host("omega")),

            LogLine(Timestamp(3000), Host("A"), Host("as")),
            LogLine(Timestamp(3300), Host("A"), Host("buki")),
            LogLine(Timestamp(3500), Host("C"), Host("vedi")),
        )
    }

    @Nested
    inner class SourcesWithOutgoingConnectionToTarget {
        @Test
        fun `connected sources in all report periods`() {
            val actionsByFilters = mapOf(incomingConnectionToTarget(Host("A")) to onOutgoingConnection(emitter))

            PeriodicReportsHandler(ListLogReader(lines), emitter, actionsByFilters).handle(
                reportPeriod = Duration(1000),
                maxTolerableLag = Duration(0),
            )

            expect(reports).containsExactly(
                ConnectionReport(incomingFrom = setOf(Host("eta")), timestamp = Timestamp(1000)),
                ConnectionReport(incomingFrom = setOf(Host("iota"), Host("nu")), timestamp = Timestamp(2000)),
                ConnectionReport(
                    incomingFrom = setOf(Host("tau"), Host("psi"), Host("omega")),
                    timestamp = Timestamp(3000)
                ),
                ConnectionReport(incomingFrom = setOf(Host("as"), Host("buki")), timestamp = Timestamp(4000)),
            )
        }

        val lines = listOf(
            LogLine(Timestamp(0), Host("alpha"), Host("B")),
            LogLine(Timestamp(100), Host("beta"), Host("C")),
            LogLine(Timestamp(200), Host("gamma"), Host("B")),
            LogLine(Timestamp(300), Host("delta"), Host("B")),
            LogLine(Timestamp(400), Host("epsilon"), Host("C")),
            LogLine(Timestamp(700), Host("zeta"), Host("B")),
            LogLine(Timestamp(800), Host("eta"), Host("A")),
            LogLine(Timestamp(900), Host("theta"), Host("B")),

            LogLine(Timestamp(1000), Host("iota"), Host("A")),
            LogLine(Timestamp(1100), Host("kappa"), Host("B")),
            LogLine(Timestamp(1200), Host("lambda"), Host("C")),
            LogLine(Timestamp(1400), Host("mu"), Host("B")),
            LogLine(Timestamp(1500), Host("nu"), Host("A")),
            LogLine(Timestamp(1600), Host("omicron"), Host("B")),
            LogLine(Timestamp(1700), Host("pi"), Host("C")),
            LogLine(Timestamp(1800), Host("rho"), Host("C")),
            LogLine(Timestamp(1900), Host("sigma"), Host("B")),

            LogLine(Timestamp(2000), Host("tau"), Host("A")),
            LogLine(Timestamp(2100), Host("upsilon"), Host("B")),
            LogLine(Timestamp(2300), Host("phi"), Host("B")),
            LogLine(Timestamp(2400), Host("chi"), Host("C")),
            LogLine(Timestamp(2500), Host("psi"), Host("A")),
            LogLine(Timestamp(2900), Host("omega"), Host("A")),

            LogLine(Timestamp(3000), Host("as"), Host("A")),
            LogLine(Timestamp(3300), Host("buki"), Host("A")),
            LogLine(Timestamp(3500), Host("vedi"), Host("C")),
        )
    }

    @Nested
    inner class TopOutgoingConnections {
        @Test
        fun `top outgoing connections`() {
            val actionsByFilters = mapOf(pass to topOutgoingConnections(emitter))

            PeriodicReportsHandler(ListLogReader(lines), emitter, actionsByFilters).handle(
                reportPeriod = Duration(1000),
                maxTolerableLag = Duration(0),
            )

            expect(reports).containsExactly(
                ConnectionReport(topOutgoing = setOf(Host("B")), timestamp = Timestamp(1000)),
                ConnectionReport(topOutgoing = setOf(Host("B")), timestamp = Timestamp(2000)),
                ConnectionReport(topOutgoing = setOf(Host("A")), timestamp = Timestamp(3000)),
                ConnectionReport(topOutgoing = setOf(Host("A"), Host("C")), timestamp = Timestamp(4000)),
            )
        }

        val lines = listOf(
            LogLine(Timestamp(0), Host("B"), Host("::")),
            LogLine(Timestamp(100), Host("C"), Host("::")),
            LogLine(Timestamp(200), Host("B"), Host("::")),
            LogLine(Timestamp(300), Host("B"), Host("::")),
            LogLine(Timestamp(400), Host("C"), Host("::")),
            LogLine(Timestamp(700), Host("B"), Host("::")),
            LogLine(Timestamp(800), Host("A"), Host("::")),
            LogLine(Timestamp(900), Host("B"), Host("::")),

            LogLine(Timestamp(1000), Host("A"), Host("::")),
            LogLine(Timestamp(1100), Host("B"), Host("::")),
            LogLine(Timestamp(1200), Host("C"), Host("::")),
            LogLine(Timestamp(1400), Host("B"), Host("::")),
            LogLine(Timestamp(1500), Host("A"), Host("::")),
            LogLine(Timestamp(1600), Host("B"), Host("::")),
            LogLine(Timestamp(1700), Host("C"), Host("::")),
            LogLine(Timestamp(1800), Host("C"), Host("::")),
            LogLine(Timestamp(1900), Host("B"), Host("::")),

            LogLine(Timestamp(2000), Host("A"), Host("::")),
            LogLine(Timestamp(2100), Host("B"), Host("::")),
            LogLine(Timestamp(2300), Host("B"), Host("::")),
            LogLine(Timestamp(2400), Host("C"), Host("::")),
            LogLine(Timestamp(2500), Host("A"), Host("::")),
            LogLine(Timestamp(2900), Host("A"), Host("::")),

            LogLine(Timestamp(3000), Host("A"), Host("::")),
            LogLine(Timestamp(3200), Host("C"), Host("::")),
            LogLine(Timestamp(3300), Host("A"), Host("::")),
            LogLine(Timestamp(3500), Host("C"), Host("::")),
        )
    }

    @Nested
    inner class CornerCases {
        @Test
        fun `repeated log lines`() {
            val actionsByFilters = mapOf(incomingConnectionToTarget(Host("A")) to onOutgoingConnection(emitter))
            val lines = listOf(
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(0), Host("beta"), Host("A")),
            )

            PeriodicReportsHandler(ListLogReader(lines), emitter, actionsByFilters).handle(
                reportPeriod = Duration(1000),
                maxTolerableLag = Duration(0),
            )

            expect(reports).containsExactly(
                ConnectionReport(
                    incomingFrom = setOf(Host("alpha"), Host("beta")),
                    timestamp = Timestamp(1000)
                )
            )
        }

        @Test
        fun `out of order log lines`() {
            val actionsByFilters = mapOf(incomingConnectionToTarget(Host("A")) to onOutgoingConnection(emitter))
            val lines = listOf(
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(200), Host("omega"), Host("B")),
                LogLine(Timestamp(197), Host("::"), Host("A")),
                LogLine(Timestamp(198), Host("beta"), Host("A")),
                LogLine(Timestamp(201), Host("gamma"), Host("A")),
                LogLine(Timestamp(198), Host("::"), Host("A")),
                LogLine(Timestamp(400), Host("psi"), Host("B")),
                LogLine(Timestamp(397), Host("::"), Host("A")),
            )

            PeriodicReportsHandler(ListLogReader(lines), emitter, actionsByFilters).handle(
                reportPeriod = Duration(500),
                maxTolerableLag = Duration(2),
            )

            expect(reports).containsExactly(
                ConnectionReport(
                    incomingFrom = setOf(Host("alpha"), Host("beta"), Host("gamma")),
                    timestamp = Timestamp(500)
                ),
            )
        }

        @Test
        fun `no log lines passing filters in reporting period`() {
            val actionsByFilters = mapOf(incomingConnectionToTarget(Host("A")) to onOutgoingConnection(emitter))
            val lines = listOf(
                LogLine(Timestamp(0), Host("omega"), Host("B")),
                LogLine(Timestamp(999), Host("omega"), Host("B")),
                LogLine(Timestamp(1000), Host("alpha"), Host("A")),
            )

            PeriodicReportsHandler(ListLogReader(lines), emitter, actionsByFilters).handle(
                reportPeriod = Duration(1000),
                maxTolerableLag = Duration(0),
            )

            expect(reports).containsExactly(
                ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(1000)),
                ConnectionReport(incomingFrom = setOf(Host("alpha")), timestamp = Timestamp(2000)),
            )
        }

        @Test
        fun `no log lines in report periods`() {
            val actionsByFilters = mapOf(incomingConnectionToTarget(Host("A")) to onOutgoingConnection(emitter))
            val lines = listOf(
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(3000), Host("beta"), Host("A")),
            )

            PeriodicReportsHandler(ListLogReader(lines), emitter, actionsByFilters).handle(
                reportPeriod = Duration(1000),
                maxTolerableLag = Duration(2),
            )

            expect(reports).containsExactly(
                ConnectionReport(incomingFrom = setOf(Host("alpha")), timestamp = Timestamp(1000)),
                ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(2000)),
                ConnectionReport(incomingFrom = emptySet(), timestamp = Timestamp(3000)),
                ConnectionReport(incomingFrom = setOf(Host("beta")), timestamp = Timestamp(4000)),
            )
        }
    }
}