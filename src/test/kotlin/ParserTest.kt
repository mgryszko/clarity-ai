import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import kotlin.test.Test

class ParserTest {
    @Test
    fun `connected hosts`() {
        val log = """Shaquera
        |Zidan
        |Adalhi
        """.trimMargin()

        val hosts = parse(log)

        expect(hosts).toBe(setOf(
            "Shaquera",
            "Zidan",
            "Adalhi",
        ))
    }

    private fun parse(log: String): Set<String> =
        log.lines().toSet()
}