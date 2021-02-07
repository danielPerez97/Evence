package daniel.perez.core.db

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.dateString(): String
{
    return this.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
}

fun LocalDateTime.timeString(): String
{
    return this.format(DateTimeFormatter.ofPattern("hh:mm a"))
}