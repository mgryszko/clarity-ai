package renderer

import periodicreports.Report
import periodicreports.ReportRenderer
import java.io.PrintStream

class PrintStreamReportRenderer(private val out: PrintStream) : ReportRenderer {
    override fun render(report: Report) {
        out.println("Incoming connections: ${report.sources.joinToString { it.name }}")
        out.println("Outgoing connections: ${report.targets.joinToString { it.name }}")
        out.println("Top outgoing connections: ${report.topOutgoingConnections.joinToString { it.name }}")
    }
}