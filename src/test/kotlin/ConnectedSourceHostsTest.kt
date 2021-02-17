import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import org.junit.jupiter.api.Nested
import kotlin.test.Test

class ConnectedSourceHostsTest {
    @Nested
    inner class ReadFindPrint {
        @Test
        fun `source hosts by timestamp range`() {
            var hosts: Set<String>? = null
            val print: (Collection<String>) -> Unit = { hosts = it.toSet() }
            val fileName = javaClass.getResource("input-file-10000.txt").path

            readFindPrint(
                logFileName = fileName,
                target = "Aaliayh",
                from = 1565656607767,
                to = 1565680778409,
                onSourceHosts = print
            )

            expect(hosts).toBe(
                setOf(
                    "Shaquera",
                    "Zidan",
                    "Adalhi",
                )
            )
        }
    }

    @Nested
    inner class FindSourceHosts {
        @Test
        fun `exact timestamps`() {
            val hosts = findSourceHosts(
                lines = lines,
                target = Host("Aaliayh"),
                from = Timestamp(1565656607767),
                to = Timestamp(1565680778409)
            )

            expect(hosts).toBe(
                setOf(
                    Host("Shaquera"),
                    Host("Zidan"),
                    Host("Adalhi"),
                )
            )
        }

        @Test
        fun `timestamps expanded range by 1ms`() {
            val hosts = findSourceHosts(
                lines = lines,
                target = Host("Aaliayh"),
                from = Timestamp(1565656607766),
                to = Timestamp(1565680778410)
            )

            expect(hosts).toBe(
                setOf(
                    Host("Shaquera"),
                    Host("Zidan"),
                    Host("Adalhi"),
                )
            )
        }

        @Test
        fun `timestamps reduced range by 1ms`() {
            val hosts = findSourceHosts(
                lines = lines,
                target = Host("Aaliayh"),
                from = Timestamp(1565656607768),
                to = Timestamp(1565680778408)
            )

            expect(hosts).toBe(setOf(Host("Zidan")))
        }

        @Test
        fun `repeated source hosts`() {
            val hosts = findSourceHosts(
                lines = sequenceOf(
                    LogLine(Timestamp(0), Host("alpha"), Host("A")),
                    LogLine(Timestamp(0), Host("alpha"), Host("A")),
                    LogLine(Timestamp(0), Host("alpha"), Host("A")),
                    LogLine(Timestamp(0), Host("beta"), Host("A")),
                ),
                target = Host("A"),
                from = Timestamp(0),
                to = Timestamp(0)
            )

            expect(hosts).toBe(setOf(Host("alpha"), Host("beta")))
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
}