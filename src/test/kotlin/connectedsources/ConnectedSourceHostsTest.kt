package connectedsources

import ch.tutteli.atrium.api.fluent.en_GB.containsExactly
import ch.tutteli.atrium.api.fluent.en_GB.notToBeNull
import ch.tutteli.atrium.api.verbs.expect
import log.Host
import log.LogLine
import log.Timestamp
import file.FileLogReader
import memory.ListLogReader
import periodicreports.CollectingReportRenderer
import periodicreports.ReportCollector
import java.io.File
import kotlin.test.Test

class HandleConnectedSourceHostsTest {
    val logFileName = javaClass.getResource("/input-file-10000.txt").path!!
    val handler = ConnectedSourceHostsHandler(FileLogReader(File(logFileName)))

    @Test
    fun `log from file, spying reports observer`() {
        var hosts: Set<Host>? = null
        val print: (Set<Host>) -> Unit = { hosts = it }

        handler.handle(
            target = "Aaliayh",
            fromMs = 1565656607767,
            toMs = 1565680778409,
            onSourceHosts = print
        )

        expect(hosts).notToBeNull().containsExactly(
            Host("Shaquera"),
            Host("Zidan"),
            Host("Adalhi"),
        )
    }
}

class FindSourceHostsTest {
    val renderer = CollectingReportRenderer()
    val emitter = ReportCollector(renderer)

    @Test
    fun `exact timestamps`() {
        var hosts = emptySet<Host>()
        ConnectedSourceHostsHandler(ListLogReader(lines.toList())).handle(
            target = Host("Aaliayh").name,
            fromMs = Timestamp(1565656607767).instant,
            toMs = Timestamp(1565680778409).instant,
            onSourceHosts = { hosts = it },
        )

        expect(hosts).containsExactly(
            Host("Shaquera"),
            Host("Zidan"),
            Host("Adalhi"),
        )
    }

    @Test
    fun `timestamps expanded range by 1ms`() {
        var hosts = emptySet<Host>()
        ConnectedSourceHostsHandler(ListLogReader(lines.toList())).handle(
            target = Host("Aaliayh").name,
            fromMs = Timestamp(1565656607766).instant,
            toMs = Timestamp(1565680778410).instant,
            onSourceHosts = { hosts = it },
        )

        expect(hosts).containsExactly(
            Host("Shaquera"),
            Host("Zidan"),
            Host("Adalhi"),
        )
    }

    @Test
    fun `timestamps reduced range by 1ms`() {
        var hosts = emptySet<Host>()
        ConnectedSourceHostsHandler(ListLogReader(lines.toList())).handle(
            target = Host("Aaliayh").name,
            fromMs = Timestamp(1565656607768).instant,
            toMs = Timestamp(1565680778408).instant,
            onSourceHosts = { hosts = it },
        )

        expect(hosts).containsExactly(Host("Zidan"))
    }

    @Test
    fun `repeated source hosts`() {
        val lines = listOf(
            LogLine(Timestamp(0), Host("alpha"), Host("A")),
            LogLine(Timestamp(0), Host("alpha"), Host("A")),
            LogLine(Timestamp(0), Host("alpha"), Host("A")),
            LogLine(Timestamp(0), Host("beta"), Host("A")),
        )
        var hosts = emptySet<Host>()
        ConnectedSourceHostsHandler(ListLogReader(lines)).handle(
            target = Host("A").name,
            fromMs = Timestamp(0).instant,
            toMs = Timestamp(0).instant,
            onSourceHosts = { hosts = it },
        )

        expect(hosts).containsExactly(Host("alpha"), Host("beta"))
    }

    val lines = sequenceOf(
        LogLine(Timestamp(1565648096156), Host("Dristen"), Host("Aadison")),
        LogLine(Timestamp(1565648978434), Host("Glorimar"), Host("Aadison")),
        LogLine(Timestamp(1565657790599), Host("Nathanael"), Host("Aadison")),
        LogLine(Timestamp(1565678376783), Host("Evelyse"), Host("Aadison")),
        LogLine(Timestamp(1565681584555), Host("Delona"), Host("Aadison")),
        LogLine(Timestamp(1565699872298), Host("Ricquan"), Host("Aadison")),
        LogLine(Timestamp(1565705846562), Host("Dominiq"), Host("Aadison")),
        LogLine(Timestamp(1565708805274), Host("Eddison"), Host("Aadison")),
        LogLine(Timestamp(1565714852680), Host("Tashaya"), Host("Aadison")),
        LogLine(Timestamp(1565716538865), Host("Kynlie"), Host("Aadison")),
        LogLine(Timestamp(1565719178421), Host("Melik"), Host("Aadison")),
        LogLine(Timestamp(1565731189448), Host("Haileyjo"), Host("Aadison")),
        LogLine(Timestamp(1565653852901), Host("Dayonte"), Host("Aaliayh")),
        LogLine(Timestamp(1565656607767), Host("Shaquera"), Host("Aaliayh")),
        LogLine(Timestamp(1565670330430), Host("Zidan"), Host("Aaliayh")),
        LogLine(Timestamp(1565680778409), Host("Adalhi"), Host("Aaliayh")),
        LogLine(Timestamp(1565681406500), Host("Terryn"), Host("Aaliayh")),
        LogLine(Timestamp(1565685740554), Host("Kyus"), Host("Aaliayh")),
        LogLine(Timestamp(1565687038949), Host("Taison"), Host("Aaliayh")),
        LogLine(Timestamp(1565699899082), Host("Cliff"), Host("Aaliayh")),
        LogLine(Timestamp(1565717675105), Host("Ivy"), Host("Aaliayh")),
        LogLine(Timestamp(1565724303383), Host("Azarel"), Host("Aaliayh")),
        LogLine(Timestamp(1565647634157), Host("Stephens"), Host("Aaronjosh")),
        LogLine(Timestamp(1565649189199), Host("Keeshaun"), Host("Aaronjosh")),
        LogLine(Timestamp(1565650785776), Host("Alexxis"), Host("Aaronjosh")),
        LogLine(Timestamp(1565652933212), Host("Makaiya"), Host("Aaronjosh")),
        LogLine(Timestamp(1565654551392), Host("Ayania"), Host("Aaronjosh")),
        LogLine(Timestamp(1565654641363), Host("Lizbett"), Host("Aaronjosh")),
        LogLine(Timestamp(1565658001662), Host("Theresamarie"), Host("Aaronjosh")),
        LogLine(Timestamp(1565663289300), Host("Taquana"), Host("Aaronjosh")),
        LogLine(Timestamp(1565679979203), Host("Akos"), Host("Aaronjosh")),
        LogLine(Timestamp(1565688191672), Host("Suhanee"), Host("Aaronjosh")),
        LogLine(Timestamp(1565718906076), Host("Jacquis"), Host("Aaronjosh")),
        LogLine(Timestamp(1565719660124), Host("Kayliyah"), Host("Aaronjosh")),
        LogLine(Timestamp(1565722415638), Host("Genai"), Host("Aaronjosh")),
        LogLine(Timestamp(1565725831679), Host("Maleya"), Host("Aaronjosh")),
    )
}