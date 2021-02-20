package renderer

import periodicreports.Report
import periodicreports.ReportRenderer
import java.io.PrintStream

class PrintStreamReportRenderer(private val out: PrintStream) : ReportRenderer {
    override fun render(report: Report) = out.println(report)
}