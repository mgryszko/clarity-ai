package periodicreports

import log.Host

data class ConnectionReport(
    val incomingFrom: Set<Host> = emptySet(),
    val outgoingTo: Set<Host> = emptySet(),
    val topOutgoing: Set<Host> = emptySet()
)