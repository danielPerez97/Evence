package teamevence.evenceapp.ical

import android.net.Uri
import androidx.core.net.toFile
import okio.buffer
import okio.source
import java.io.File
import java.time.Month
import java.time.ZonedDateTime

object Parser
{
    private var builder = ICalSpec.Builder()
    private var state: State? = null
    private var eventBuilder: EventSpec.Builder? = null

    fun parse(uri: Uri): ICalSpec
    {
        return parse( uri.toFile() )
    }

    fun parse(file: File): ICalSpec
    {
        var line = ""
        file.source().buffer().use {
            while (it.readUtf8Line()?.let { line = it } != null)
            {
                if (line == "BEGIN:VCALENDAR")
                {
                    state = State.VCalendar
                }
                if(line == "END:VCALENDAR")
                {
                    state = State.Finished
                }
                if(state != null && state != State.Finished)
                    builder = handleLine(line)
            }
        }
        val ical = builder.build()
        builder = ICalSpec.Builder()
        state = null
        eventBuilder = null
        return ical
    }

    private fun handleLine(line: String): ICalSpec.Builder = when (state!!)
    {
        State.VCalendar ->
        {
            when (line)
            {
                "BEGIN:VTIMEZONE" ->
                {
                    state = State.ParsingTimeZone
                }
                "BEGIN:VEVENT" ->
                {
                    state = State.ParsingEvent
                }
            }
            builder
        }
        State.ParsingTimeZone ->
        {
            if (line.startsWith("TZID:"))
            {
                when (line.substringAfter(":"))
                {
                    "America/Chicago" -> builder = builder.timeZone(TimeZones.AMERICA_CHICAGO)
                }
            }
            else if(line == "END:VTIMEZONE")
            {
                state = State.VCalendar
            }
            builder
        }
        State.ParsingEvent ->
        {
            when
            {
                line.startsWith("UID:") ->
                {
                    val uidUntilAtSymbol = line.substringBefore("@")
                    val id = uidUntilAtSymbol.substring(uidUntilAtSymbol.lastIndex, uidUntilAtSymbol.lastIndex + 1).toInt()
                    eventBuilder = EventSpec.Builder(id)
                }
                line.startsWith("DTSTART") ->
                {
                    eventBuilder = eventBuilder!!.start(dtToZonedTime(line))
                }
                line.startsWith("DTEND") ->
                {
                    eventBuilder = eventBuilder!!.end(dtToZonedTime(line))
                }
                line.startsWith("SUMMARY:") ->
                {
                    eventBuilder = eventBuilder!!.title(line.substringAfter(":"))
                }
                line.startsWith("DESCRIPTION:") ->
                {
                    eventBuilder = eventBuilder!!.description(line.substringAfter(":"))
                }
                line.startsWith("LOCATION:") ->
                {
                    eventBuilder = eventBuilder!!.location(line.substringAfter(":"))
                }
                line == "END:VEVENT" ->
                {
                    builder = builder.addEvent(eventBuilder!!.build())
                    state = State.VCalendar
                }
            }
            builder
        }
        State.Finished ->
        {
            builder
        }
    }

    private fun determineHour(hour: String): Int
    {
        val intHour = hour.toInt()
        return if (intHour > 12)
            intHour - 12
        else
            intHour
    }

    private fun dtToZonedTime(dt: String): ZonedDateTime
    {
        val date = dt.substringAfter(":")
        val year = date.substring(0, 4).toInt()
        val month = date.substring(4, 6)
        val dayStr = date.substring(6, 8)
        val day = dayStr.dePad()

        val time = date.substringAfterLast("T")
        val hour = time.substring(0, 2)
        val minute = time.substring(2, 4).toInt()
        val seconds = time.substring(4, 6).toInt()

        return ZonedDateTime.now()
                .withMonth(Month.of(month.toInt()).value)
                .withDayOfMonth(day)
                .withYear(year)
                .withHour(hour.toInt())
                .withMinute(minute)
                .withSecond(seconds)
    }

    private sealed class State
    {
        object VCalendar : State()
        object ParsingTimeZone : State()
        object ParsingEvent : State()
        object Finished : State()
    }

    private fun String.dePad(): Int
    {
        return if (this.length > 1 && this[0].toString() == "0")
        {
            this.substring(1, this.lastIndex + 1).toInt()
        } else
        {
            this.toInt()
        }
    }
}