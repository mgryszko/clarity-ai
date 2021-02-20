package periodicreports

import ch.tutteli.atrium.api.fluent.en_GB.contains
import ch.tutteli.atrium.api.fluent.en_GB.containsExactly
import ch.tutteli.atrium.api.fluent.en_GB.feature
import ch.tutteli.atrium.api.fluent.en_GB.hasSize
import ch.tutteli.atrium.api.fluent.en_GB.inAnyOrder
import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import file.FileLogReader
import log.Duration
import log.Host
import log.Timestamp
import java.io.File
import kotlin.math.exp
import kotlin.test.Test

class PeriodicReportsAcceptanceTest {
    val renderer = CollectingReportRenderer()
    val emitter = ReportCollector(renderer)
    val logFile = File(javaClass.getResource("/input-file-10000.txt").path!!)
    val handler = PeriodicReportsHandler(
        logReader = FileLogReader(logFile, Duration(0)),
        emitter = emitter,
        actionsByFilters = mapOf(
            incomingConnectionToTarget(Host("Aaliayh")) to onOutgoingConnection(emitter),
            outgoingConnectionFromSource(Host("Shaian")) to onIncomingConnection(emitter),
            pass to topOutgoingConnections(emitter),
        )
    )
    val oneHour = Duration(60 * 60 * 1000)
    val fiveMinutes = Duration(5 * 60 * 1000)

    @Test
    fun handle() {
        handler.handle(
            reportPeriod = oneHour,
            maxTolerableLag = fiveMinutes
        )

        expect(renderer.reports).containsExactly(
            ConnectionReport(
                timestamp = Timestamp(1565650804351),
                topOutgoing = setOf(
                    Host("Keimy"),
                    Host("Kishauna"),
                    Host("Eveliz"),
                    Host("Traysen"),
                    Host("Lachandra"),
                    Host("Santangelo"),
                    Host("Donnis")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565654404351),
                incomingFrom = setOf(Host("Dayonte")),
                topOutgoing = setOf(
                    Host("Coretta"),
                    Host("Maveric"),
                    Host("Zayda"),
                    Host("Donnisha"),
                    Host("Malka"),
                    Host("Jamauria"),
                    Host("Ziqi"),
                    Host("Allana")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565658004351),
                incomingFrom = setOf(Host("Shaquera")),
                topOutgoing = setOf(
                    Host("Justina"),
                    Host("Carmell"),
                    Host("Sharlie"),
                    Host("Lashondra"),
                    Host("Manar"),
                    Host("Shareny"),
                    Host("Raeqwon"),
                    Host("Aketzaly"),
                    Host("Lynelle")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565661604351),
                topOutgoing = setOf(Host("Damerius"), Host("Darmesha"), Host("Avellina"), Host("Azlynne"), Host("Aanchal"), Host("Kelela")),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565665204351),
                outgoingTo = setOf(Host("Prue")),
                topOutgoing = setOf(
                    Host("Jenelly"),
                    Host("Zowii"),
                    Host("Elenia"),
                    Host("Michelli"),
                    Host("Emanuela"),
                    Host("Lamora"),
                    Host("Terika"),
                    Host("Naralie"),
                    Host("Katiria"),
                    Host("Lawrencia"),
                    Host("Zynah")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565668804351),
                topOutgoing = setOf(
                    Host("Amrom"),
                    Host("Syire"),
                    Host("Irian"),
                    Host("Zakyrah"),
                    Host("Zarhianna"),
                    Host("Shyonna"),
                    Host("Dequann"),
                    Host("Asmar"),
                    Host("Davahn"),
                    Host("Eja"),
                    Host("Hara")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565672404351),
                incomingFrom = setOf(Host("Zidan")),
                topOutgoing = setOf(Host("Amadu"), Host("Nealie"), Host("Migual"), Host("Aunya"), Host("Ranaa")),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565676004351),
                topOutgoing = setOf(Host("Aixa"), Host("Yuritzy"), Host("Jivan")),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565679604351),
                topOutgoing = setOf(
                    Host("Khadarius"),
                    Host("Andry"),
                    Host("Wenona"),
                    Host("Deira"),
                    Host("Zaelee"),
                    Host("Brookelyne"),
                    Host("Jahmall"),
                    Host("Mayher"),
                    Host("Babacar"),
                    Host("Aneika"),
                    Host("Zaydi"),
                    Host("Endora")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565683204351),
                incomingFrom = setOf(Host("Adalhi"), Host("Terryn")),
                topOutgoing = setOf(
                    Host("Nyoka"),
                    Host("Terryn"),
                    Host("Janicia"),
                    Host("Macall"),
                    Host("Anaee"),
                    Host("Javien"),
                    Host("Dasjia"),
                    Host("Shaughnessy")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565686804351),
                incomingFrom = setOf(Host("Kyus")),
                topOutgoing = setOf(
                    Host("Tija"),
                    Host("Tuvia"),
                    Host("Chanci"),
                    Host("Isaiaha"),
                    Host("Sage"),
                    Host("Jaylen"),
                    Host("Hidayah"),
                    Host("Shirin"),
                    Host("Tyzon"),
                    Host("Janaina"),
                    Host("Stphanie"),
                    Host("Zantasia")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565690404351),
                incomingFrom = setOf(Host("Taison")),
                topOutgoing = setOf(
                    Host("Annaleya"),
                    Host("Kaylynn"),
                    Host("Quatashia"),
                    Host("Cirsten"),
                    Host("Emerlynn"),
                    Host("Bohen"),
                    Host("Amecia"),
                    Host("Jarran"),
                    Host("Andric"),
                    Host("Obaloluwa"),
                    Host("Augustina")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565694004351),
                topOutgoing = setOf(
                    Host("Roxxanne"),
                    Host("Jedric"),
                    Host("Bram"),
                    Host("Vern"),
                    Host("Rayniel"),
                    Host("Renyah"),
                    Host("Nefertiti")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565697604351),
                outgoingTo = setOf(Host("Heshy")),
                topOutgoing = setOf(
                    Host("Macky"),
                    Host("Daijon"),
                    Host("Jonette"),
                    Host("Danelly"),
                    Host("Sahibjot"),
                    Host("Shanzay"),
                    Host("Raimundo"),
                    Host("Laquante"),
                    Host("Arriana")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565701204351),
                incomingFrom = setOf(Host("Cliff")),
                topOutgoing = setOf(Host("Kamyree")),
                topOutgoingNumber = 3
            ),
            ConnectionReport(
                timestamp = Timestamp(1565704804351),
                outgoingTo = setOf(Host("Maryanne")),
                topOutgoing = setOf(
                    Host("Mykiya"),
                    Host("Zakyrah"),
                    Host("Keanah"),
                    Host("Ladiamond"),
                    Host("Akzel"),
                    Host("Londale"),
                    Host("Thristan"),
                    Host("Aileena"),
                    Host("Alonie"),
                    Host("Aia")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565708404351),
                topOutgoing = setOf(
                    Host("Aliese"),
                    Host("Jaeceon"),
                    Host("Eswin"),
                    Host("Mckeena"),
                    Host("Sharlie"),
                    Host("Marybell"),
                    Host("Creek"),
                    Host("Dristen"),
                    Host("Agim"),
                    Host("Mykenzi"),
                    Host("Alaan")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565712004351),
                topOutgoing = setOf(Host("Adoniz"), Host("Pashence"), Host("Rubyrose"), Host("Jaymere"), Host("Deliany"), Host("Yaditzel")),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565715604351),
                topOutgoing = setOf(
                    Host("Kaisley"),
                    Host("Diamantina"),
                    Host("Nikaylah"),
                    Host("Dakari"),
                    Host("Lamya"),
                    Host("Anousha"),
                    Host("Sea"),
                    Host("Harshitha"),
                    Host("Heydi"),
                    Host("Zacory"),
                    Host("Lamika"),
                    Host("Karver"),
                    Host("Loryn"),
                    Host("Jonathin")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565719204351),
                incomingFrom = setOf(Host("Ivy")),
                topOutgoing = setOf(Host("Devang"), Host("Shanessa"), Host("Charlisa"), Host("Brecker"), Host("Daeun"), Host("Melik")),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565722804351),
                outgoingTo = setOf(Host("Rownan")),
                topOutgoing = setOf(
                    Host("Markaysia"),
                    Host("Machaela"),
                    Host("Maecy"),
                    Host("Montray"),
                    Host("Thijs"),
                    Host("Chanteria"),
                    Host("Zeryk"),
                    Host("Taliea"),
                    Host("Desiah"),
                    Host("Kayaan"),
                    Host("Jnasia"),
                    Host("Daejion"),
                    Host("Ziyang")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(
                timestamp = Timestamp(1565726404351),
                incomingFrom = setOf(Host("Azarel")),
                outgoingTo = setOf(Host("Kesler")),
                topOutgoing = setOf(
                    Host("Allimae"),
                    Host("Tighe"),
                    Host("Juliett"),
                    Host("Junious"),
                    Host("Kainyn"),
                    Host("Darnasia"),
                    Host("Infinity"),
                    Host("Ran"),
                    Host("Rebeca")
                ),
                topOutgoingNumber = 2
            ),
            ConnectionReport(timestamp = Timestamp(1565730004351), topOutgoing = setOf(Host("Dewana")), topOutgoingNumber = 3),
            ConnectionReport(
                timestamp = Timestamp(1565733604351),
                topOutgoing = setOf(
                    Host("Burnett"),
                    Host("Ishi"),
                    Host("Saphyra"),
                    Host("Kingsly"),
                    Host("Kiaeem"),
                    Host("Kahleil"),
                    Host("Gaetan"),
                    Host("Katrinna"),
                    Host("Jaiceyon"),
                    Host("Dashyra")
                ),
                topOutgoingNumber = 2
            ),
        )
    }
}