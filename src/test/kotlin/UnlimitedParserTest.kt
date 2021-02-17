import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import kotlin.test.Test

class UnlimitedParserTest {
    @Test
    fun `connected sources in all windows`() {
        val hosts = connectedSourceHosts(
            lines = lines,
            target = Host("Aadison"),
            initialTimestamp = Timestamp(0),
            reportWindow = 1000,
            maxTolerableLag = 0,
        )

        expect(hosts).toBe(
            listOf(
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
        )
    }

    @Test
    fun `repeated source hosts`() {
        val hosts = connectedSourceHosts(
            lines = sequenceOf(
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(0), Host("beta"), Host("A")),
            ),
            target = Host("A"),
            initialTimestamp = Timestamp(0),
            reportWindow = 1000,
            maxTolerableLag = 0,
        )

        expect(hosts).toBe(listOf(setOf(Host("alpha"), Host("beta"))))
    }

    @Test
    fun `out of order entries`() {
        val hosts = connectedSourceHosts(
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
            target = Host("A"),
            initialTimestamp = Timestamp(0),
            reportWindow = 500,
            maxTolerableLag = 2,
        )

        expect(hosts).toBe(
            listOf(
                setOf(
                    Host("alpha"),
                    Host("beta"),
                    Host("gamma"),
                ),
            )
        )
    }

    @Test
    fun `no connected sources in a window`() {
        val hosts = connectedSourceHosts(
            lines = sequenceOf(
                LogLine(Timestamp(0), Host("omega"), Host("B")),
                LogLine(Timestamp(999), Host("omega"), Host("B")),
                LogLine(Timestamp(1000), Host("alpha"), Host("A")),
            ),
            target = Host("A"),
            initialTimestamp = Timestamp(0),
            reportWindow = 1000,
            maxTolerableLag = 0,
        )

        expect(hosts).toBe(
            listOf(
                emptySet(),
                setOf(Host("alpha")),
            )
        )
    }

    @Test
    fun `no log lines in a window`() {
        val hosts = connectedSourceHosts(
            lines = sequenceOf(
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(3000), Host("beta"), Host("A")),
            ),
            target = Host("A"),
            initialTimestamp = Timestamp(0),
            reportWindow = 1000,
            maxTolerableLag = 2,
        )

        expect(hosts).toBe(
            listOf(
                setOf(Host("alpha")),
                emptySet(),
                emptySet(),
                setOf(Host("beta")),
            )
        )
    }

    @Test
    fun `log lines before initial timestamp`() {
        val hosts = connectedSourceHosts(
            lines = sequenceOf(
                LogLine(Timestamp(0), Host("alpha"), Host("A")),
                LogLine(Timestamp(1), Host("omega"), Host("B")),
                LogLine(Timestamp(2), Host("beta"), Host("A")),
            ),
            target = Host("A"),
            initialTimestamp = Timestamp(2),
            reportWindow = 1000,
            maxTolerableLag = 0,
        )

        expect(hosts).toBe(listOf(setOf(Host("beta"))))
    }

    private fun connectedSourceHosts(
        lines: Sequence<LogLine>,
        target: Host,
        initialTimestamp: Timestamp,
        reportWindow: Long,
        maxTolerableLag: Long
    ): List<Set<Host>> {
        val hosts = mutableSetOf<Host>()
        val reports = mutableListOf<Set<Host>>()
        var nextWindow = initialTimestamp.instant + reportWindow
        var timestampHighWatermark = initialTimestamp.instant
        lines.forEach {
            if (it.timestamp.instant >= nextWindow) {
                reports.add(hosts.toSet())
                hosts.clear()
                if (it.timestamp.instant / nextWindow > 1) {
                    repeat((it.timestamp.instant / nextWindow).toInt() - 1) {
                        reports.add(emptySet())
                    }
                }
                nextWindow += reportWindow
            }
            if (it.timestamp.instant >= timestampHighWatermark - maxTolerableLag) {
                if (it.target == target) {
                    hosts.add(it.source)
                }
            }
            if (it.timestamp.instant > timestampHighWatermark) {
                timestampHighWatermark = it.timestamp.instant
            }
        }
        reports.add(hosts.toSet())
        return reports
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