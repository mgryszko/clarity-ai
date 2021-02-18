import ch.tutteli.atrium.api.fluent.en_GB.containsExactly
import ch.tutteli.atrium.api.fluent.en_GB.notToBeNull
import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import kotlin.test.Test

class HandlePeriodicReportsTest {
    @Test
    fun `log from file, spying connected host obsrver`() {
        var hosts: List<Set<Host>>? = null
        val print: (List<Set<Host>>) -> Unit = { hosts = it }
        val logFileName = javaClass.getResource("input-file-10000.txt").path

        handlePeriodicReports(
            logFileName = logFileName,
            host = "Aaliayh",
            reportPeriodMs = 60 * 60 * 1000,
            maxTolerableLagMs = 5 * 60 * 1000,
            onReports = print
        )

        expect(hosts).notToBeNull().containsExactly(
            emptySet(),
            setOf(Host("Dayonte")),
            setOf(Host("Shaquera")),
            emptySet(),
            emptySet(),
            emptySet(),
            setOf(Host("Zidan")),
            emptySet(),
            emptySet(),
            setOf(Host("Adalhi"), Host("Terryn")),
            setOf(Host("Kyus")),
            setOf(Host("Taison")),
            emptySet(),
            emptySet(),
            setOf(Host("Cliff")),
            emptySet(),
            emptySet(),
            emptySet(),
            emptySet(),
            setOf(Host("Ivy")),
            emptySet(),
            setOf(Host("Azarel")),
            emptySet(),
            emptySet(),
        )
    }
}

class PeriodicReportsTest {
    val collector = ReportCollector()

    @Test
    fun `connected sources in all report periods`() {
        generatePeriodicReports(
            reportCollector = collector,
            lines = lines,
            host = Host("Aadison"),
            initialTimestamp = Timestamp(0),
            reportPeriod = Duration(1000),
            maxTolerableLag = Duration(0),
        )

        expect(collector.reports).containsExactly(
            setOf(
                Host("Eddison"),
            ),
            setOf(
                Host("Glorimar"),
                Host("Tashaya"),
            ),
            setOf(
                Host("Delona"),
                Host("Haileyjo"),
                Host("Evelyse"),
            ),
            setOf(
                Host("Nathanael"),
                Host("Ricquan"),
            ),
        )
    }

    @Test
    fun `repeated source hosts`() {
        generatePeriodicReports(
            lines = sequenceOf(
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(0), Host("beta"), Host("A")),
            ),
            host = Host("A"),
            initialTimestamp = Timestamp(0),
            reportPeriod = Duration(1000),
            maxTolerableLag = Duration(0),
            reportCollector = collector,
        )

        expect(collector.reports).containsExactly(setOf(Host("alpha"), Host("beta")))
    }

    @Test
    fun `out of order entries`() {
        generatePeriodicReports(
            lines = sequenceOf(
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(200), Host("omega"), Host("B")),
                LogLine(Timestamp(197), Host("::"), Host("A")),
                LogLine(Timestamp(198), Host("beta"), Host("A")),
                LogLine(Timestamp(201), Host("gamma"), Host("A")),
                LogLine(Timestamp(198), Host("::"), Host("A")),
                LogLine(Timestamp(400), Host("psi"), Host("B")),
                LogLine(Timestamp(397), Host("::"), Host("A")),
            ),
            host = Host("A"),
            initialTimestamp = Timestamp(0),
            reportPeriod = Duration(500),
            maxTolerableLag = Duration(2),
            reportCollector = collector,
        )

        expect(collector.reports).containsExactly(
            setOf(
                Host("alpha"),
                Host("beta"),
                Host("gamma"),
            )
        )
    }

    @Test
    fun `no connected sources in reporting period`() {
        generatePeriodicReports(
            lines = sequenceOf(
                LogLine(Timestamp(0), Host("omega"), Host("B")),
                LogLine(Timestamp(999), Host("omega"), Host("B")),
                LogLine(Timestamp(1000), Host("alpha"), Host("A")),
            ),
            host = Host("A"),
            initialTimestamp = Timestamp(0),
            reportPeriod = Duration(1000),
            maxTolerableLag = Duration(0),
            reportCollector = collector,
        )

        expect(collector.reports).containsExactly(
            emptySet(),
            setOf(Host("alpha")),
        )
    }

    @Test
    fun `no log lines in report periods`() {
        generatePeriodicReports(
            lines = sequenceOf(
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(3000), Host("beta"), Host("A")),
            ),
            host = Host("A"),
            initialTimestamp = Timestamp(0),
            reportPeriod = Duration(1000),
            maxTolerableLag = Duration(2),
            reportCollector = collector,
        )

        expect(collector.reports).containsExactly(
            setOf(Host("alpha")),
            emptySet(),
            emptySet(),
            setOf(Host("beta")),
        )
    }

    @Test
    fun `log lines before initial timestamp`() {
        generatePeriodicReports(
            lines = sequenceOf(
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(1), Host("omega"), Host("B")),
                LogLine(Timestamp(2), Host("beta"), Host("A")),
            ),
            host = Host("A"),
            initialTimestamp = Timestamp(2),
            reportPeriod = Duration(1000),
            maxTolerableLag = Duration(0),
            reportCollector = collector,
        )

        expect(collector.reports).toBe(listOf(setOf(Host("beta"))))
    }

    val lines = sequenceOf(
        LogLine(Timestamp(0), Host("Akos"), Host("Aaronjosh")),
        LogLine(Timestamp(100), Host("Shaquera"), Host("Aaliayh")),
        LogLine(Timestamp(200), Host("Jacquis"), Host("Aaronjosh")),
        LogLine(Timestamp(300), Host("Keeshaun"), Host("Aaronjosh")),
        LogLine(Timestamp(400), Host("Terryn"), Host("Aaliayh")),
        LogLine(Timestamp(700), Host("Theresamarie"), Host("Aaronjosh")),
        LogLine(Timestamp(800), Host("Eddison"), Host("Aadison")),
        LogLine(Timestamp(900), Host("Makaiya"), Host("Aaronjosh")),
        LogLine(Timestamp(1000), Host("Glorimar"), Host("Aadison")),
        LogLine(Timestamp(1100), Host("Ayania"), Host("Aaronjosh")),
        LogLine(Timestamp(1200), Host("Dayonte"), Host("Aaliayh")),
        LogLine(Timestamp(1400), Host("Suhanee"), Host("Aaronjosh")),
        LogLine(Timestamp(1500), Host("Tashaya"), Host("Aadison")),
        LogLine(Timestamp(1600), Host("Taquana"), Host("Aaronjosh")),
        LogLine(Timestamp(1700), Host("Kyus"), Host("Aaliayh")),
        LogLine(Timestamp(1800), Host("Azarel"), Host("Aaliayh")),
        LogLine(Timestamp(1900), Host("Stephens"), Host("Aaronjosh")),
        LogLine(Timestamp(2000), Host("Delona"), Host("Aadison")),
        LogLine(Timestamp(2100), Host("Kayliyah"), Host("Aaronjosh")),
        LogLine(Timestamp(2300), Host("Alexxis"), Host("Aaronjosh")),
        LogLine(Timestamp(2400), Host("Cliff"), Host("Aaliayh")),
        LogLine(Timestamp(2500), Host("Haileyjo"), Host("Aadison")),
        LogLine(Timestamp(2900), Host("Evelyse"), Host("Aadison")),
        LogLine(Timestamp(3000), Host("Nathanael"), Host("Aadison")),
        LogLine(Timestamp(3300), Host("Ricquan"), Host("Aadison")),
        LogLine(Timestamp(3500), Host("Adalhi"), Host("Aaliayh")),
    )
}