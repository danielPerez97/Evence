package projects.csce.evence.ical

import okio.buffer
import okio.sink
import java.io.File
import java.util.*

class ICalSpec private constructor(builder: Builder)
{
	val events: List<EventSpec> = builder.events
	val fileName: String = builder.fileName!!
	private val prodId: String = "-//University of Arkansas"

	fun text(): String
	{
		val sb = StringBuilder()

		sb.appendln("BEGIN:VCALENDAR")
		sb.appendln("VERSION:2.0")
		sb.appendln("PRODID:$prodId")
		sb.appendln("""
			BEGIN:VTIMEZONE
			TZID:America/Chicago
			X-LIC-LOCATION:America/Chicago
			BEGIN:DAYLIGHT
			TZOFFSETFROM:-0600
			TZOFFSETTO:-0500
			TZNAME:CDT
			DTSTART:19700308T020000
			RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU
			END:DAYLIGHT
			BEGIN:STANDARD
			TZOFFSETFROM:-0500
			TZOFFSETTO:-0600
			TZNAME:CST
			DTSTART:19701101T020000
			RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU
			END:STANDARD
			END:VTIMEZONE
		""".trimIndent())
		events.forEach { sb.appendln(it.text()) }
		sb.appendln("END:VCALENDAR")

		return sb.toString()
	}

	class Builder internal constructor()
	{
		internal val events: MutableList<EventSpec> = mutableListOf()
		internal var fileName: String? = null

		fun fileName(fileName: String) = apply { this.fileName = fileName }

		fun addEvent(vararg event: EventSpec) = apply { events += event }

		fun build(): ICalSpec
		{
			// Check if the EventSpec ID's are unique
			if(events.map { it.id }.distinct().count() != events.size)
			{
				throw IdsNotUniqueException()
			}

			// Check that a filename has been given
			Objects.requireNonNull(fileName, "fileName == null")

			// Return the ICalSpec
			return ICalSpec(this)
		}
	}

	companion object
	{
		@JvmStatic fun builder(): Builder = Builder()
	}

	class IdsNotUniqueException: Throwable()
}