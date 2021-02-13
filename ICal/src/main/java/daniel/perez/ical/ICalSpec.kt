package daniel.perez.ical

import java.util.Objects


data class ICalSpec private constructor(val builder: Builder)
{
	val events: List<EventSpec> = builder.events
	val timeZone: TimeZones = builder.timeZone

	private val prodId: String = "-//University of Arkansas"

	fun text(): String
	{
		val sb = StringBuilder()
		val timeZoneString: String
		when(timeZone)
		{
			TimeZones.AMERICA_CHICAGO -> timeZoneString = TimeZone.americaChicago
		}

		sb.appendln("BEGIN:VCALENDAR")
		sb.appendln("VERSION:2.0")
		sb.appendln("PRODID:$prodId")
		sb.appendln(timeZoneString)
		events.forEach { sb.appendln(it.text()) }
		sb.appendln("END:VCALENDAR")
		sb.appendln()

		return sb.toString()
	}

	class Builder constructor()
	{
		internal val events: MutableList<EventSpec> = mutableListOf()
		internal var timeZone: TimeZones = TimeZones.AMERICA_CHICAGO

		fun addEvent(vararg event: EventSpec): Builder = apply { events += event }

		fun timeZone(timeZone: TimeZones): Builder = apply { this.timeZone = timeZone }

		fun build(): ICalSpec
		{
			// Check if the EventSpec ID's are unique
			if(events.map { it.id }.distinct().count() != events.size)
			{
				throw IdsNotUniqueException()
			}

			// Return the ICalSpec
			return ICalSpec(this)
		}
	}

	class IdsNotUniqueException: Throwable()

	private object TimeZone
	{
		val americaChicago: String = """
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
		""".trimIndent()
	}
}