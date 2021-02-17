inline class Host(val name: String)

inline class Timestamp(val instant: Long) {
    operator fun plus(duration: Duration): Timestamp = Timestamp(instant + duration.ms)
    operator fun div(other: Timestamp): Long = instant / other.instant
    operator fun compareTo(other: Timestamp): Int = instant.compareTo(other.instant)
    operator fun minus(other: Duration): Timestamp = Timestamp(instant - other.ms)
}

inline class Duration(val ms: Long)

data class LogLine(val timestamp: Timestamp, val source: Host, val target: Host)
