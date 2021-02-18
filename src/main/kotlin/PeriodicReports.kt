fun main(args: Array<String>) {
    val logFileName = args[0]
    val host = args[1]
    val reportPeriodMs = args[2].toLong()
    val maxTolerableLagMs = args[3].toLong()

    PeriodicReportsHandler(FileLogReader(logFileName), ReportCollector(::println)).handle(
        Host(host),
        Duration(reportPeriodMs),
        Duration(maxTolerableLagMs)
    )
}
