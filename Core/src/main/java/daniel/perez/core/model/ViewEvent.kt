package daniel.perez.core.model

import android.graphics.Bitmap
import daniel.perez.core.getDay
import daniel.perez.core.getLocaleMonth
import daniel.perez.core.getYear
import daniel.perez.core.toZonedDateTime
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec

data class ViewEvent(
        val id: Long,
        val title: String,
        val description: String,
        val startDate: String,
        val startTime: String,
        val endDate: String,
        val endTime: String,
        val location: String
)
{
    fun iCalText(): String
    {
        val start = Time(startDate, startTime)
        val end = Time(endDate, endTime)
        val event = EventSpec.Builder(0)
                .title(title)
                .description(description)
                .location(location)
                .start(toZonedDateTime(start.month, start.dayOfMonth, start.year, start.hour, start.minute))
                .end(toZonedDateTime(end.month, end.dayOfMonth, end.year, end.hour, end.minute))
                .build()

        val ical = ICalSpec.Builder()
                .fileName(title)
                .addEvent(event)
                .build()

        return ical.text()
    }

    fun startEpochMilli(): Long
    {
        return 0
    }

    fun endEpochMilli(): Long
    {
        return 0
    }

    private class Time(date: String, time: String)
    {
        val month: Int = getLocaleMonth(date).toInt()
        val dayOfMonth: Int = getDay(date).toInt()
        val year: Int = getYear(date).toInt()
        val hour: Int = time.split(":")[0].toInt()
        val minute: Int = time.substringAfter(":").split(" ")[0].toInt()
    }
}