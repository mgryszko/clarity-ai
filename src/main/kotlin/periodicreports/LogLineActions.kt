package periodicreports

import log.LogLine

typealias LogLineAction = (LogLine) -> Unit

fun onSourceConnected(collector: ReportCollector): LogLineAction = { collector.sourceHostConnected(it.source) }
