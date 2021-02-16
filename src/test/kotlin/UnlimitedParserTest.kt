import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import kotlin.test.Test

class UnlimitedParserTest {
    @Test
    fun `connected sources in first window`() {
        val hosts = connectedSourceHosts(
            lines = lines,
            target = Host("Aadison"),
            window = 1000
        )

        expect(hosts).toBe(
            setOf(
                Host("Eddison"),
            )
        )
    }

    private fun connectedSourceHosts(lines: Sequence<LogLine>, target: Host, window: Int): Set<Host> {
        val hosts = mutableListOf<Host>()
        val nextWindowStart = 0 + window
        lines.forEach {
            if (it.target == target && it.timestamp.instant < nextWindowStart) hosts.add(it.source)
        }
        return hosts.toSet()
    }

    val lines = sequenceOf(
        LogLine(Timestamp(0), Host("Akos"), Host("Aaronjosh")),
        LogLine(Timestamp(100), Host("Shaquera"), Host("Aaliayh")),
        LogLine(Timestamp(200), Host("Jacquis"), Host("Aaronjosh")),
        LogLine(Timestamp(300), Host("Keeshaun"), Host("Aaronjosh")),
        LogLine(Timestamp(400), Host("Terryn"), Host("Aaliayh")),
        LogLine(Timestamp(500), Host("Taison"), Host("Aaliayh")),
        LogLine(Timestamp(600), Host("Maleya"), Host("Aaronjosh")),
        LogLine(Timestamp(700), Host("Theresamarie"), Host("Aaronjosh")),
        LogLine(Timestamp(800), Host("Eddison"), Host("Aadison")),
        LogLine(Timestamp(900), Host("Makaiya"), Host("Aaronjosh")),
        LogLine(Timestamp(1000), Host("Glorimar"), Host("Aadison")),
        LogLine(Timestamp(1100), Host("Ayania"), Host("Aaronjosh")),
        LogLine(Timestamp(1200), Host("Dayonte"), Host("Aaliayh")),
        LogLine(Timestamp(1300), Host("Lizbett"), Host("Aaronjosh")),
        LogLine(Timestamp(1400), Host("Suhanee"), Host("Aaronjosh")),
        LogLine(Timestamp(1500), Host("Tashaya"), Host("Aadison")),
        LogLine(Timestamp(1600), Host("Taquana"), Host("Aaronjosh")),
        LogLine(Timestamp(1700), Host("Kyus"), Host("Aaliayh")),
        LogLine(Timestamp(1800), Host("Azarel"), Host("Aaliayh")),
        LogLine(Timestamp(1900), Host("Stephens"), Host("Aaronjosh")),
        LogLine(Timestamp(2000), Host("Delona"), Host("Aadison")),
        LogLine(Timestamp(2100), Host("Kayliyah"), Host("Aaronjosh")),
        LogLine(Timestamp(2200), Host("Melik"), Host("Aadison")),
        LogLine(Timestamp(2300), Host("Alexxis"), Host("Aaronjosh")),
        LogLine(Timestamp(2400), Host("Cliff"), Host("Aaliayh")),
        LogLine(Timestamp(2500), Host("Haileyjo"), Host("Aadison")),
        LogLine(Timestamp(2600), Host("Dristen"), Host("Aadison")),
        LogLine(Timestamp(2700), Host("Genai"), Host("Aaronjosh")),
        LogLine(Timestamp(2800), Host("Dominiq"), Host("Aadison")),
        LogLine(Timestamp(2900), Host("Evelyse"), Host("Aadison")),
        LogLine(Timestamp(3000), Host("Nathanael"), Host("Aadison")),
        LogLine(Timestamp(3100), Host("Kynlie"), Host("Aadison")),
        LogLine(Timestamp(3200), Host("Ivy"), Host("Aaliayh")),
        LogLine(Timestamp(3300), Host("Ricquan"), Host("Aadison")),
        LogLine(Timestamp(3400), Host("Zidan"), Host("Aaliayh")),
        LogLine(Timestamp(3500), Host("Adalhi"), Host("Aaliayh")),    )
}