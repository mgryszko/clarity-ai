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
                ConnectionReport(emptySet()),
                ConnectionReport(setOf(Host("Dayonte"))),
                ConnectionReport(setOf(Host("Shaquera"))),
                ConnectionReport(emptySet()),
                ConnectionReport(emptySet()),
                ConnectionReport(emptySet()),
                ConnectionReport(setOf(Host("Zidan"))),
                ConnectionReport(emptySet()),
                ConnectionReport(emptySet()),
                ConnectionReport(setOf(Host("Adalhi"), Host("Terryn"))),
                ConnectionReport(setOf(Host("Kyus"))),
                ConnectionReport(setOf(Host("Taison"))),
                ConnectionReport(emptySet()),
                ConnectionReport(emptySet()),
                ConnectionReport(setOf(Host("Cliff"))),
                ConnectionReport(emptySet()),
                ConnectionReport(emptySet()),
                ConnectionReport(emptySet()),
                ConnectionReport(emptySet()),
                ConnectionReport(setOf(Host("Ivy"))),
                ConnectionReport(emptySet()),
                ConnectionReport(setOf(Host("Azarel"))),
                ConnectionReport(emptySet()),
                ConnectionReport(emptySet()),
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
                ConnectionReport(outgoingTo = setOf(Host("eta"))),
                ConnectionReport(outgoingTo = setOf(Host("iota"), Host("nu"))),
                ConnectionReport(outgoingTo = setOf(Host("tau"), Host("psi"), Host("omega"))),
                ConnectionReport(outgoingTo = setOf(Host("as"), Host("buki"))),
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
                ConnectionReport(incomingFrom = setOf(Host("eta"))),
                ConnectionReport(incomingFrom = setOf(Host("iota"), Host("nu"))),
                ConnectionReport(incomingFrom = setOf(Host("tau"), Host("psi"), Host("omega"))),
                ConnectionReport(incomingFrom = setOf(Host("as"), Host("buki"))),
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
                ConnectionReport(topOutgoing = setOf(Host("B"))),
                ConnectionReport(topOutgoing = setOf(Host("B"))),
                ConnectionReport(topOutgoing = setOf(Host("A"))),
                ConnectionReport(topOutgoing = setOf(Host("A"), Host("C"))),
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

            expect(reports).containsExactly(ConnectionReport(incomingFrom = setOf(Host("alpha"), Host("beta"))))
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
                ConnectionReport(incomingFrom = setOf(Host("alpha"), Host("beta"), Host("gamma"))),
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
                ConnectionReport(incomingFrom = emptySet()),
                ConnectionReport(incomingFrom = setOf(Host("alpha"))),
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
                ConnectionReport(incomingFrom = setOf(Host("alpha"))),
                ConnectionReport(incomingFrom = emptySet()),
                ConnectionReport(incomingFrom = emptySet()),
                ConnectionReport(incomingFrom = setOf(Host("beta"))),
            )
        }
    }
}