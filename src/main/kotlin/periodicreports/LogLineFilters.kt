package periodicreports

import log.Host
import log.LogLine

typealias LogLineFilter = (LogLine) -> Boolean

fun incomingConnectionToTarget(target: Host): LogLineFilter = { it.target == target }

fun outgoingConnectionFromSource(source: Host): LogLineFilter = { it.source == source }

val pass: LogLineFilter = { true }