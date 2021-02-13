package daniel.perez.core.model

import android.net.Uri
import daniel.perez.core.toZonedDateTime
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec
import java.time.LocalDateTime
import java.util.*

data class ViewEvent(
        val id: Long,
        val title: String,
        val description: String,
        val location: String,
        val startDateTime: LocalDateTime,
        val endDateTime: LocalDateTime,
        val imageUri: Uri
)
{
    fun iCalSpec(): ICalSpec
    {
        val event = EventSpec.Builder(0)
                .title(title)
                .description(description)
                .location(location)
                .start(toZonedDateTime(startDateTime.monthValue, startDateTime.dayOfMonth, startDateTime.year, startDateTime.hour, startDateTime.minute))
                .end(toZonedDateTime(endDateTime.monthValue, endDateTime.dayOfMonth, endDateTime.year, endDateTime.hour, endDateTime.minute))
                .build()

        return ICalSpec.Builder()
                .addEvent(event)
                .build()
    }

    fun startDatePretty(): String
    {
        return startDateTime.pretty()
    }

    fun endDatePretty(): String
    {
        return endDateTime.pretty()
    }

    fun startYear(): Int
    {
        return startDateTime.year
    }

    fun startMonth(): Int
    {
        return startDateTime.monthValue
    }

    fun startDayOfMonth(): Int
    {
        return startDateTime.dayOfMonth
    }

    fun startHour(): Int
    {
        return startDateTime.hour
    }

    fun startMinute(): Int
    {
        return startDateTime.minute
    }

    fun startEpochMilli(): Long
    {
        return 0
    }

    fun endYear(): Int
    {
        return endDateTime.year
    }

    fun endMonth(): Int
    {
        return endDateTime.monthValue
    }

    fun endDayOfMonth(): Int
    {
        return endDateTime.dayOfMonth
    }

    fun endHour(): Int
    {
        return endDateTime.hour
    }

    fun endMinute(): Int
    {
        return endDateTime.minute
    }

    fun endEpochMilli(): Long
    {
        return 0
    }

    fun LocalDateTime.pretty(): String
    {
        val month = this.month.toString()
        return "${month.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)} ${this.dayOfMonth}, ${this.year}"
    }
}