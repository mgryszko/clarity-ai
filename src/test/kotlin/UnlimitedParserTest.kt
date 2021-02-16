import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import kotlin.test.Test

class UnlimitedParserTest {
    @Test
    fun `connected sources in all windows`() {
        val hosts = connectedSourceHosts(
            lines = lines,
            target = Host("Aadison"),
            initialInterval = Timestamp(0),
            reportInterval = 1000,
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

    private fun connectedSourceHosts(lines: Sequence<LogLine>, target: Host, initialInterval: Timestamp, reportInterval: Long): List<Set<Host>> {
        val hosts = mutableSetOf<Host>()
        val reports = mutableListOf<Set<Host>>()
        var nextWindowStart = initialInterval.instant + reportInterval
        lines.forEach {
            if (it.timestamp.instant >= nextWindowStart) {
                reports.add(hosts.toSet())
                hosts.clear()
                nextWindowStart += reportInterval
            }

            if (it.target == target && it.timestamp.instant < nextWindowStart) hosts.add(it.source)
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
        LogLine(Timestamp(3500), Host("Adalhi"), Host("Aaliayh")),    )
}