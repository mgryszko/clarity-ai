package renderer

import ch.tutteli.atrium.api.fluent.en_GB.contains
import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import log.Host
import org.junit.jupiter.api.Test
import periodicreports.Report
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class PrintStreamReportRendererTest {
    val out = ByteArrayOutputStream()
    val stream = PrintStream(out, true)
    val renderer = PrintStreamReportRenderer(PrintStream(out, true))

    @Test
    fun render() {
        val report = Report(
            sources = setOf(Host("A"), Host("B")),
            targets = setOf(Host("C"), Host("D"), Host("E")),
            topOutgoingConnections = setOf(Host("F"), Host("G")),
        )

        renderer.render(report)

        val subject = out.toString()
        expect(subject) {
            contains("Incoming connections: A, B")
            contains("Outgoing connections: C, D, E")
            contains("Top outgoing connections: F, G")
        }
    }
}