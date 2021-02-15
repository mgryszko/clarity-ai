import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import kotlin.test.Test

class ParserTest {
    @Test
    fun `connected hosts`() {
        val log = """
            |Shaquera Aaliayh
            |Dristen Aadison
            |Stephens Aaronjosh
            |Zidan Aaliayh
            |Glorimar Aadison
            |Keeshaun Aaronjosh
            |Adalhi Aaliayh
            |Nathanael Aadison
            |Alexxis Aaronjosh
        """.trimMargin()

        val hosts = parse(log, "Aaliayh")

        expect(hosts).toBe(
            setOf(
                "Shaquera",
                "Zidan",
                "Adalhi",
            )
        )
    }

    private fun parse(log: String, host: String): Set<String> =
        log.lines()
            .filter { it.split(" ")[1] == host }
            .map { it.split(" ")[0] }
            .toSet()
}