class ReportCollector {
    private val sourceHosts = mutableSetOf<Host>()
    private val _reports = mutableListOf<Set<Host>>()
    val reports: List<Set<Host>>
        get() = _reports.toList()

    fun sourceHostConnected(source: Host) {
        sourceHosts.add(source)
    }

    fun closeReport() {
        _reports.add(sourceHosts.toSet())
        sourceHosts.clear()
    }

    @Suppress("ForEachParameterNotUsed")
    fun closeEmptyReports(count: Long) {
        (0 until count).forEach { _reports.add(emptySet()) }
    }
}
