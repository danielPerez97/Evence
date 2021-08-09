package daniel.perez.ical


data class ICalSpec private constructor(val builder: Builder)
{
	val events: List<EventSpec> = builder.events
	val timeZone: TimeZones = builder.timeZone
	val prodId: String = builder.prodId

	fun text(): String
	{
		val tzString = getTimeZoneString(timeZone)

		return buildString {
			appendLine("BEGIN:VCALENDAR")
			appendLine("VERSION:2.0")
			appendLine("PRODID:$prodId")
			appendLine(tzString)
			events.forEach { appendLine(it.text()) }
			appendLine("END:VCALENDAR")
			appendLine()
		}
	}

	fun getTimeZoneString(timeZone: TimeZones): String
	{
		return when(timeZone) {
			TimeZones.AMERICA_CHICAGO -> TimeZone.americaChicago
			else -> throw Exception("Time zone not compatible")
		}
	}

	class Builder constructor() {

		internal var prodId: String = "-//University of Arkansas"
		internal val events: MutableList<EventSpec> = mutableListOf()
		internal var timeZone: TimeZones = TimeZones.AMERICA_CHICAGO

		fun productionId(prodId: String) = apply { this.prodId = prodId }

		fun addEvent(vararg event: EventSpec): Builder = apply { events += event }

		fun timeZone(timeZone: TimeZones): Builder = apply { this.timeZone = timeZone }

		fun build(): ICalSpec {
			// Check if the EventSpec ID's are unique
			if (events.map { it.id }.distinct().count() != events.size) {
				throw IdsNotUniqueException()
			}

			// Return the ICalSpec
			return ICalSpec(this)
		}
	}

	class IdsNotUniqueException : Throwable()

	object TimeZone
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