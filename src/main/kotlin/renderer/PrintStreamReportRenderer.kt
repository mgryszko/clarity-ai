package renderer

import periodicreports.ConnectionReport
import periodicreports.ReportRenderer
import java.io.PrintStream
import java.time.Instant

class PrintStreamReportRenderer(private val out: PrintStream) : ReportRenderer {
    override fun render(report: ConnectionReport) {
        out.println("Report on: ${Instant.ofEpochMilli(report.timestamp.instant)} (${report.timestamp.instant})")
        out.println("- incoming connections: ${report.incomingFrom.joinToString { it.name }}")
        out.println("- outgoing connections: ${report.outgoingTo.joinToString { it.name }}")
        out.println("- top outgoing connections: ${report.topOutgoing.joinToString { it.name }}")
    }
}