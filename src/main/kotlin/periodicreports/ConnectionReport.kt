package periodicreports

import log.Host
import log.Timestamp

data class ConnectionReport(
    val timestamp: Timestamp,
    val incomingFrom: Set<Host> = emptySet(),
    val outgoingTo: Set<Host> = emptySet(),
    val topOutgoing: Set<Host> = emptySet(),
)