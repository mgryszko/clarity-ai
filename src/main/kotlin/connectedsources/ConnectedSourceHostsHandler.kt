package connectedsources

import kotlinx.coroutines.runBlocking
import log.Host
import log.LogReader
import log.Timestamp

class ConnectedSourceHostsHandler(private val logReader: LogReader) {
    fun handle(target: String, fromMs: Long, toMs: Long, onSourceHosts: (Set<Host>) -> Unit) {
        val hosts = mutableSetOf<Host>()
        runBlocking {
            logReader.readLines { (timestamp, source, lineTarget) ->
                if (lineTarget == Host(target) && timestamp >= Timestamp(fromMs) && timestamp <= Timestamp(toMs)) {
                    hosts.add(source)
                }
            }
        }

        onSourceHosts(hosts)
    }
}