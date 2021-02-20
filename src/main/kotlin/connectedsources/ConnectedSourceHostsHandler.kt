package connectedsources

import kotlinx.coroutines.runBlocking
import log.Host
import log.LogReader
import log.Timestamp

class ConnectedSourceHostsHandler(private val logReader: LogReader) {
    fun handle(target: Host, from: Timestamp, to: Timestamp, onSourceHosts: (Set<Host>) -> Unit) {
        val hosts = mutableSetOf<Host>()
        runBlocking {
            logReader.readLines { (timestamp, source, lineTarget) ->
                if (lineTarget == target && timestamp >= from && timestamp <= to) {
                    hosts.add(source)
                }
            }
        }

        onSourceHosts(hosts)
    }
}