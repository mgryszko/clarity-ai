package periodicreports

import log.Host
import log.LogLine

typealias LogLineFilter = (LogLine) -> Boolean

fun connectedToTarget(target: Host): LogLineFilter = { it.target == target }
