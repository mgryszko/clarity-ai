package periodicreports

import log.Host
import log.LogLine

typealias LogLineFilter = (LogLine) -> Boolean

fun connectedToTarget(target: Host): LogLineFilter = { it.target == target }

fun receivedConnectionFromSource(source: Host): LogLineFilter = { it.source == source }

val pass: LogLineFilter = { true }