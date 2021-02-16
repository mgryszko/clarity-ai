import java.io.File
import java.security.SecureRandom

private object ClasspathReader {
    fun readLines(fileName: String): List<String> =
        File(javaClass.getResource(fileName).path).readLines()
}

fun main(args: Array<String>) {
    val logFileName = args[0]
    val count = args[1].toInt()

    val sources = ClasspathReader.readLines("sources.txt")
    val targets = ClasspathReader.readLines("targets.txt")
    val rnd = SecureRandom()

    File(logFileName).bufferedWriter().use { writer ->
        repeat(count) { i ->
            writer.write("%s %s %s".format(i, sources[rnd.nextInt(sources.size)], targets[rnd.nextInt(targets.size)]))
            writer.newLine()
        }
    }
}
