package periodicreports

import log.LogLine

typealias LogLineAction = (LogLine) -> Unit

fun onOutgoingConnection(collector: ReportCollector): LogLineAction = { collector.outgoingConnectionFrom(it.source) }

fun onIncomingConnection(collector: ReportCollector): LogLineAction = { collector.incomingConnectionTo(it.target) }

fun topOutgoingConnections(collector: ReportCollector): LogLineAction = { collector.topOutgoingConnections(it.source) }
