import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import org.junit.jupiter.api.Nested
import kotlin.test.Test

class ParserTest {
    @Nested
    inner class ReadParsePrint {
        @Test
        fun `exact timestamps`() {
            var hosts: Set<String>? = null
            val print: (Set<String>) -> Unit = { hosts = it }
            val fileName = javaClass.getResource("input-file-10000.txt").path

            readParsePrint(
                logFileName = fileName,
                connectedTo = "Aaliayh",
                from = 1565656607767,
                to = 1565680778409,
                onConnectedHosts = print
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
    inner class Parse {
        @Test
        fun `exact timestamps`() {
            val hosts = parse(lines = lines, connectedTo = "Aaliayh", from = 1565656607767, to = 1565680778409)

            expect(hosts).toBe(
                setOf(
                    "Shaquera",
                    "Zidan",
                    "Adalhi",
                )
            )
        }

        @Test
        fun `timestamps expanded range by 1ms`() {
            val hosts = parse(lines = lines, connectedTo = "Aaliayh", from = 1565656607766, to = 1565680778410)

            expect(hosts).toBe(
                setOf(
                    "Shaquera",
                    "Zidan",
                    "Adalhi",
                )
            )
        }

        @Test
        fun `timestamps reduced range by 1ms`() {
            val hosts = parse(lines = lines, connectedTo = "Aaliayh", from = 1565656607768, to = 1565680778408)

            expect(hosts).toBe(setOf("Zidan"))
        }

        val lines = sequenceOf(
            "1565648096156 Dristen Aadison",
            "1565648978434 Glorimar Aadison",
            "1565657790599 Nathanael Aadison",
            "1565678376783 Evelyse Aadison",
            "1565681584555 Delona Aadison",
            "1565699872298 Ricquan Aadison",
            "1565705846562 Dominiq Aadison",
            "1565708805274 Eddison Aadison",
            "1565714852680 Tashaya Aadison",
            "1565716538865 Kynlie Aadison",
            "1565719178421 Melik Aadison",
            "1565731189448 Haileyjo Aadison",
            "1565653852901 Dayonte Aaliayh",
            "1565656607767 Shaquera Aaliayh",
            "1565670330430 Zidan Aaliayh",
            "1565680778409 Adalhi Aaliayh",
            "1565681406500 Terryn Aaliayh",
            "1565685740554 Kyus Aaliayh",
            "1565687038949 Taison Aaliayh",
            "1565699899082 Cliff Aaliayh",
            "1565717675105 Ivy Aaliayh",
            "1565724303383 Azarel Aaliayh",
            "1565647634157 Stephens Aaronjosh",
            "1565649189199 Keeshaun Aaronjosh",
            "1565650785776 Alexxis Aaronjosh",
            "1565652933212 Makaiya Aaronjosh",
            "1565654551392 Ayania Aaronjosh",
            "1565654641363 Lizbett Aaronjosh",
            "1565658001662 Theresamarie Aaronjosh",
            "1565663289300 Taquana Aaronjosh",
            "1565679979203 Akos Aaronjosh",
            "1565688191672 Suhanee Aaronjosh",
            "1565718906076 Jacquis Aaronjosh",
            "1565719660124 Kayliyah Aaronjosh",
            "1565722415638 Genai Aaronjosh",
            "1565725831679 Maleya Aaronjosh",
        )
    }
}