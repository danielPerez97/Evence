package daniel.perez.core.model

import android.graphics.Bitmap
import daniel.perez.core.getDay
import daniel.perez.core.getLocaleMonth
import daniel.perez.core.getYear
import daniel.perez.core.toZonedDateTime
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec
import timber.log.Timber
import java.time.LocalDate
import java.time.Month
import java.util.*

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

    fun startYear(): Int
    {
        return getYear(startDate).toInt()
    }

    fun startMonth(): Int
    {
        return month(startDate)
    }

    fun startDayOfMonth(): Int
    {
        return getDay(startDate).toInt()
    }

    fun startHour(): Int
    {
        val time = Time(startDate, startTime)
        return time.hour
    }

    fun startMinute(): Int
    {
        val time = Time(startDate, startTime)
        return time.minute
    }

    fun startEpochMilli(): Long
    {
        return 0
    }

    fun endYear(): Int
    {
        return getYear(endDate).toInt()
    }

    fun endMonth(): Int
    {
        return month(endDate)
    }

    fun endDayOfMonth(): Int
    {
        return getDay(endDate).toInt()
    }

    fun endHour(): Int
    {
        val time = Time(endDate, endTime)
        return time.hour
    }

    fun endMinute(): Int
    {
        val time = Time(endDate, endTime)
        return time.minute
    }

    fun endEpochMilli(): Long
    {
        return 0
    }

    private fun month(date: String): Int
    {
        return Month.valueOf( getLocaleMonth( date ).toUpperCase(Locale.ROOT) ).value
    }

    private class Time(date: String, time: String)
    {
        val month: Int = Month.valueOf( getLocaleMonth(date).toUpperCase(Locale.ROOT) ).value
        val dayOfMonth: Int = getDay(date).toInt()
        val year: Int = getYear(date).toInt()
        val hour: Int = time.split(":")[0].toInt()
        val minute: Int = time.substringAfter(":").split(" ")[0].toInt()
    }
}