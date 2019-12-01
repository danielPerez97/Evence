package projects.csce.evence.ical

import okio.buffer
import okio.source
import org.threeten.bp.Month
import org.threeten.bp.ZonedDateTime
import java.io.File

object Parser
{
    private var builder = ICalSpec.Builder()
    private var state: State? = null
    private var eventBuilder: EventSpec.Builder? = null


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
                builder = handleLine(line)
            }
        }
        val ical = builder.fileName(file.nameWithoutExtension).build()
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
                    val id = uidUntilAtSymbol[uidUntilAtSymbol.lastIndex].toInt()
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
        val year = date.substring(0, 3).toInt()
        val month = date.substring(4, 5).toInt()
        val day = date.substring(6, 7).toInt()

        val time = date.substringAfterLast("T")
        val hour = time.substring(0, 1)
        val minute = time.substring(2, 3).toInt()
        val seconds = time.substring(4,5).toInt()

        return ZonedDateTime.now()
                .withMonth(Month.of(month).value)
                .withDayOfMonth(day)
                .withYear(year)
                .withHour(determineHour(hour))
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
}