package renderer

import periodicreports.Report
import periodicreports.ReportRenderer

class PrintStreamReportRenderer : ReportRenderer {
    override fun render(report: Report) = println(report)
}