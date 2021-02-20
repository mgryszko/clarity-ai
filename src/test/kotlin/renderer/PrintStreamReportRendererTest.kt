package renderer

import ch.tutteli.atrium.api.fluent.en_GB.contains
import ch.tutteli.atrium.api.verbs.expect
import log.Host
import log.Timestamp
import org.junit.jupiter.api.Test
import periodicreports.ConnectionReport
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class PrintStreamReportRendererTest {
    val out = ByteArrayOutputStream()
    val renderer = PrintStreamReportRenderer(PrintStream(out, true))

    @Test
    fun render() {
        val report = ConnectionReport(
            timestamp = Timestamp(1613806916032),
            incomingFrom = setOf(Host("A"), Host("B")),
            outgoingTo = setOf(Host("C"), Host("D"), Host("E")),
            topOutgoing = setOf(Host("F"), Host("G")),
            topOutgoingNumber = 3,
        )

        renderer.render(report)

        val subject = out.toString()
        expect(subject) {
            contains("Report on: 2021-02-20T07:41:56")
            contains("1613806916032")
            contains("incoming connections: A, B")
            contains("outgoing connections: C, D, E")
            contains("top outgoing connections: 3, from: F, G")
        }
    }
}