import java.io.File

fun main(args: Array<String>) {
    val logFileName = args[0]

    File(logFileName).bufferedReader().use { reader ->
        var waits = 10
        while (waits > 0) {
            val line = reader.readLine()
            if (line != null) {
                println(line)
                waits = 10
            } else {
                waits -= 1
                Thread.sleep(1000)
            }
        }
    }
}
