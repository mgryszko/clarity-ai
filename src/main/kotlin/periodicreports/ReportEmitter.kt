package periodicreports

import log.Timestamp

interface ReportEmitter {
    fun emitReport(timestamp: Timestamp)

    fun emitEmptyReport(timestamp: Timestamp)
}