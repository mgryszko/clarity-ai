package main

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.long
import file.FileLogReader
import log.Duration
import log.Host
import log.LogLine
import log.Timestamp
import periodicreports.PeriodicReportsHandler
import periodicreports.ReportCollector
import periodicreports.incomingConnectionToTarget
import periodicreports.onIncomingConnection
import periodicreports.onOutgoingConnection
import periodicreports.outgoingConnectionFromSource
import periodicreports.pass
import periodicreports.topOutgoingConnections
import renderer.PrintStreamReportRenderer
import java.io.File

class ConnectedSourceHosts : CliktCommand() {
    private val logFileName: String by argument(name = "LOG_FILE_NAME", help = "log file name")
    private val target: String by argument(help = "search target of incoming connections")
    private val fromMs: Long by option("-f", "--from", help = "timestamp from inclusive").long().default(0)
    private val toMs: Long by option("-t", "--to", help = "timestamp to inclusive").long().default(Long.MAX_VALUE)

    override fun run() {
        handleConnectedSourceHosts(logFileName, target, fromMs, toMs, ::println)
    }
}

fun main(args: Array<String>) = ConnectedSourceHosts().main(args)

fun handleConnectedSourceHosts(logFileName: String, target: String, fromMs: Long, toMs: Long, onSourceHosts: (Set<Host>) -> Unit) {
    val hosts = read(logFileName) { lines ->
        val parsedLines = parse(lines)
        findSourceHosts(
            lines = parsedLines,
            target = Host(target),
            from = Timestamp(fromMs),
            to = Timestamp(toMs)
        )
    }

    onSourceHosts(hosts)
}

private fun <T> read(fileName: String, processLines: (Sequence<String>) -> T): T =
    File(fileName).useLines(Charsets.UTF_8, processLines)

private fun parse(lines: Sequence<String>): Sequence<LogLine> =
    lines.filter(String::isNotBlank).map(::parse)

private fun parse(line: String): LogLine {
    val (timestamp, source, target) = line.split(" ")
    return LogLine(timestamp = Timestamp(timestamp.toLong()), source = Host(source), target = Host(target))
}

fun findSourceHosts(
    lines: Sequence<LogLine>,
    target: Host,
    from: Timestamp,
    to: Timestamp
): Set<Host> = lines
    .filter { (timestamp, _, lineTarget) ->
        lineTarget == target && timestamp >= from && timestamp <= to
    }
    .map(LogLine::source)
    .toSet()
