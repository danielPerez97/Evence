package daniel.perez.evencedb.data

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

fun LocalDateTime.toJavaTime(): java.time.LocalDateTime
{
    return java.time.LocalDateTime.of(
            year, Month.of(monthNumber), dayOfMonth, hour, minute, second, nanosecond
    )
}