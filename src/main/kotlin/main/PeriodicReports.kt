package main

import file.FileLogReader
import log.Duration
import log.Host
import periodicreports.PeriodicReportsHandler
import periodicreports.ReportCollector
import java.io.File

fun main(args: Array<String>) {
    val logFileName = args[0]
    val host = args[1]
    val reportPeriodMs = args[2].toLong()
    val maxTolerableLagMs = args[3].toLong()
    val timeoutMs = 30000.toLong()

    PeriodicReportsHandler(FileLogReader(File(logFileName), Duration(timeoutMs)), ReportCollector(::println)).handle(
        Host(host),
        Duration(reportPeriodMs),
        Duration(maxTolerableLagMs)
    )
}
