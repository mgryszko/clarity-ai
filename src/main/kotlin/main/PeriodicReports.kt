package main

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.long
import file.FileLogReader
import log.Duration
import log.Host
import periodicreports.PeriodicReportsHandler
import periodicreports.ReportCollector
import periodicreports.incomingConnectionToTarget
import periodicreports.onOutgoingConnection
import java.io.File

class PeriodicReports : CliktCommand() {
    private val oneHour = (60 * 60 * 1000).toLong()
    private val fiveMinutes = (5 * 60 * 1000).toLong()
    private val thirtySeconds = (30 * 1000).toLong()

    private val logFileName: String by argument(name = "LOG_FILE_NAME", help = "log file name")
    private val host: String by argument(help = "host for reports")
    private val reportFreqMs: Long by option("-f", "--frequency", help = "reporting frequency").long().default(oneHour)
    private val maxTolerableLagMs: Long by option("-l", "--max-lag", help = "maximum tolerable lag of log entries")
        .long().default(fiveMinutes)
    private val timeoutMs: Long by option("-t", "--timeout", help = "log inactivity timeout").long().default(thirtySeconds)

    override fun run() {
        PeriodicReportsHandler(
            logReader = FileLogReader(File(logFileName), Duration(timeoutMs)),
            emitter = ReportCollector(::println),
            actionsByFilters = mapOf(
                incomingConnectionToTarget(Host(host)) to onOutgoingConnection(ReportCollector(::println))
            )
        ).handle(
            Duration(reportFreqMs),
            Duration(maxTolerableLagMs)
        )
    }
}

fun main(args: Array<String>) = PeriodicReports().main(args)
