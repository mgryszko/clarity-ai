package periodicreports

import log.Host

data class Report(val sources: Set<Host> = emptySet(), val targets: Set<Host> = emptySet(), val topOutgoingConnections: Host? = null)