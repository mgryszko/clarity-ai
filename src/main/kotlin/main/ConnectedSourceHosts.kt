package main

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.long
import connectedsources.ConnectedSourceHostsHandler
import file.FileLogReader
import log.Host
import log.Timestamp
import java.io.File

class ConnectedSourceHosts : CliktCommand() {
    private val logFileName: String by argument(name = "LOG_FILE_NAME", help = "log file name")
    private val target: String by argument(help = "search target of incoming connections")
    private val fromMs: Long by option("-f", "--from", help = "timestamp from inclusive").long().default(0)
    private val toMs: Long by option("-t", "--to", help = "timestamp to inclusive").long().default(Long.MAX_VALUE)

    override fun run() {
        ConnectedSourceHostsHandler(FileLogReader(File(logFileName)))
            .handle(Host(target), Timestamp(fromMs), Timestamp(toMs)) { println(it) }
    }
}

fun main(args: Array<String>) = ConnectedSourceHosts().main(args)

