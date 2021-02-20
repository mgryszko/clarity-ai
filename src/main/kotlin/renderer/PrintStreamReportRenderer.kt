package renderer

import periodicreports.ConnectionReport
import periodicreports.ReportRenderer
import java.io.PrintStream
import java.time.Instant

class PrintStreamReportRenderer(private val out: PrintStream) : ReportRenderer {
    override fun render(report: ConnectionReport) {
        report.apply {
            out.println("Report on: ${Instant.ofEpochMilli(timestamp.instant)} (${timestamp.instant})")
            out.println("- incoming connections: ${incomingFrom.joinToString { it.name }}")
            out.println("- outgoing connections: ${outgoingTo.joinToString { it.name }}")
            out.println("- top outgoing connections: ${topOutgoingNumber}, from: ${topOutgoing.joinToString { it.name }}")
        }
    }
}