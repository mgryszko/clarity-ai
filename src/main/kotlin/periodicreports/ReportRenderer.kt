package periodicreports

@FunctionalInterface
interface ReportRenderer {
    fun render(report: ConnectionReport)
}