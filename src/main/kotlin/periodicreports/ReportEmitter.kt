package periodicreports

interface ReportEmitter {
    fun emitReport()

    fun emitEmptyReports(count: Long)
}