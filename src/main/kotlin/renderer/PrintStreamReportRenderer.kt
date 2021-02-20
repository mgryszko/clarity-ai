package renderer

import periodicreports.ConnectionReport
import periodicreports.ReportRenderer
import java.io.PrintStream

class PrintStreamReportRenderer(private val out: PrintStream) : ReportRenderer {
    override fun render(report: ConnectionReport) {
        out.println("Incoming connections: ${report.incomingFrom.joinToString { it.name }}")
        out.println("Outgoing connections: ${report.outgoingTo.joinToString { it.name }}")
        out.println("Top outgoing connections: ${report.topOutgoing.joinToString { it.name }}")
    }
}