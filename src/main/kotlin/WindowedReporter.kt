fun connectedSourceHosts(
    lines: Sequence<LogLine>,
    target: Host,
    initialTimestamp: Timestamp,
    reportWindow: Duration,
    maxTolerableLag: Duration,
): List<Set<Host>> {
    val hosts = mutableSetOf<Host>()
    val reports = mutableListOf<Set<Host>>()
    var nextWindow = initialTimestamp + reportWindow
    var timestampHighWatermark = initialTimestamp
    lines.forEach { (timestamp, source, lineTarget) ->
        if (timestamp >= nextWindow) {
            reports.add(hosts.toSet())
            hosts.clear()
            if (timestamp / nextWindow > 1) {
                repeat((timestamp / nextWindow).toInt() - 1) {
                    reports.add(emptySet())
                }
            }
            nextWindow += reportWindow
        }
        if (timestamp >= timestampHighWatermark - maxTolerableLag) {
            if (lineTarget == target) {
                hosts.add(source)
            }
        }
        if (timestamp > timestampHighWatermark) {
            timestampHighWatermark = timestamp
        }
    }
    reports.add(hosts.toSet())
    return reports
}

