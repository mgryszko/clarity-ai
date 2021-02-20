package connectedsources

import ch.tutteli.atrium.api.fluent.en_GB.isEmpty
import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import log.Host
import log.LogLine
import log.Timestamp
import memory.ListLogReader
import kotlin.test.Test

class ConnectedSourceHostsHandlerTest {
    @Test
    fun `exact timestamps`() {
        val hosts = ConnectedSourceHostsHandler(ListLogReader(lines)).handle(
            target = Host("A"),
            from = Timestamp(130),
            to = Timestamp(150),
        )

        expect(hosts).toBe(
            setOf(
                Host("13"),
                Host("14"),
                Host("15"),
            )
        )
    }

    @Test
    fun `timestamps expanded range by 1ms`() {
        val hosts = ConnectedSourceHostsHandler(ListLogReader(lines)).handle(
            target = Host("A"),
            from = Timestamp(129),
            to = Timestamp(151),
        )

        expect(hosts).toBe(
            setOf(
                Host("13"),
                Host("14"),
                Host("15"),
            )
        )
    }

    @Test
    fun `timestamps reduced range by 1ms`() {
        val hosts = ConnectedSourceHostsHandler(ListLogReader(lines)).handle(
            target = Host("A"),
            from = Timestamp(131),
            to = Timestamp(149),
        )

        expect(hosts).toBe(setOf(Host("14")))
    }

    @Test
    fun `host not found`() {
        val hosts = ConnectedSourceHostsHandler(ListLogReader(lines)).handle(
            target = Host("notFound"),
            from = Timestamp(0),
            to = Timestamp(Long.MAX_VALUE),
        )

        expect(hosts).isEmpty()
    }

    @Test
    fun `repeated source hosts`() {
        val lines = listOf(
            LogLine(Timestamp(0), Host("alpha"), Host("A")),
            LogLine(Timestamp(0), Host("alpha"), Host("A")),
            LogLine(Timestamp(0), Host("alpha"), Host("A")),
            LogLine(Timestamp(0), Host("beta"), Host("A")),
        )
        val hosts = ConnectedSourceHostsHandler(ListLogReader(lines)).handle(
            target = Host("A"),
            from = Timestamp(0),
            to = Timestamp(0),
        )

        expect(hosts).toBe(setOf(Host("alpha"), Host("beta")))
    }

    @Test
    fun `empty log`() {
        val hosts = ConnectedSourceHostsHandler(ListLogReader(emptyList())).handle(
            target = Host("any"),
            from = Timestamp(0),
            to = Timestamp(0),
        )

        expect(hosts).isEmpty()
    }

    val lines = listOf(
        LogLine(Timestamp(90), Host("9"), Host("B")),
        LogLine(Timestamp(140), Host("14"), Host("A")),
        LogLine(Timestamp(210), Host("21"), Host("A")),
        LogLine(Timestamp(330), Host("33"), Host("C")),
        LogLine(Timestamp(160), Host("16"), Host("A")),
        LogLine(Timestamp(150), Host("15"), Host("A")),
        LogLine(Timestamp(100), Host("10"), Host("B")),
        LogLine(Timestamp(170), Host("17"), Host("A")),
        LogLine(Timestamp(50), Host("5"), Host("B")),
        LogLine(Timestamp(270), Host("27"), Host("C")),
        LogLine(Timestamp(300), Host("30"), Host("C")),
        LogLine(Timestamp(180), Host("18"), Host("A")),
        LogLine(Timestamp(70), Host("7"), Host("B")),
        LogLine(Timestamp(30), Host("3"), Host("B")),
        LogLine(Timestamp(80), Host("8"), Host("B")),
        LogLine(Timestamp(240), Host("24"), Host("C")),
        LogLine(Timestamp(340), Host("34"), Host("C")),
        LogLine(Timestamp(20), Host("2"), Host("B")),
        LogLine(Timestamp(10), Host("1"), Host("B")),
        LogLine(Timestamp(200), Host("20"), Host("A")),
        LogLine(Timestamp(40), Host("4"), Host("B")),
        LogLine(Timestamp(350), Host("35"), Host("C")),
        LogLine(Timestamp(120), Host("12"), Host("A")),
        LogLine(Timestamp(130), Host("13"), Host("A")),
        LogLine(Timestamp(230), Host("23"), Host("C")),
        LogLine(Timestamp(110), Host("11"), Host("B")),
        LogLine(Timestamp(0), Host("0"), Host("B")),
        LogLine(Timestamp(280), Host("28"), Host("C")),
        LogLine(Timestamp(320), Host("32"), Host("C")),
        LogLine(Timestamp(60), Host("6"), Host("B")),
        LogLine(Timestamp(290), Host("29"), Host("C")),
        LogLine(Timestamp(310), Host("31"), Host("C")),
        LogLine(Timestamp(220), Host("22"), Host("C")),
        LogLine(Timestamp(250), Host("25"), Host("C")),
        LogLine(Timestamp(190), Host("19"), Host("A")),
        LogLine(Timestamp(260), Host("26"), Host("C")),
    )
}