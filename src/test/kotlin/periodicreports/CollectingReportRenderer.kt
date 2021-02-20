package periodicreports

class CollectingReportRenderer : ReportRenderer {
    private val _reports = mutableListOf<ConnectionReport>()
    val reports: List<ConnectionReport>
        get() = _reports.toList()

    override fun render(report: ConnectionReport) {
        _reports.add(report)
    }
}