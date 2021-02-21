package connectedsources

import log.Host
import log.LogReader
import log.Timestamp

class ConnectedSourceHostsHandler(private val logReader: LogReader) {
    suspend fun handle(target: Host, from: Timestamp, to: Timestamp): Set<Host> {
        val hosts = mutableSetOf<Host>()
        logReader.readLines { (timestamp, source, lineTarget) ->
            if (lineTarget == target && timestamp >= from && timestamp <= to) {
                hosts.add(source)
            }
        }
        return hosts
    }
}