package periodicreports

import log.LogLine

typealias LogLineAction = (LogLine) -> Unit

fun onSourceConnected(collector: ReportCollector): LogLineAction = { collector.sourceHostConnected(it.source) }

fun onConnectedToTarget(collector: ReportCollector): LogLineAction = { collector.connectedToTarget(it.target) }

fun topOutgoingConnections(collector: ReportCollector): LogLineAction = { collector.topOutgoingConnections(it.source) }
