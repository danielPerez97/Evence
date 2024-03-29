package teamevence.evenceapp.ical

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class EventSpec private constructor(builder: Builder)
{
	val id: Int = builder.id
	val title: String = builder.title!!
	val description = builder.description ?: ""
	val location: String = builder.location ?: ""
	val start: ZonedDateTime = builder.start!!
	val end: ZonedDateTime = builder.end!!

	data class Builder constructor(internal val id: Int)
	{
		internal var title: String? = null
		internal var description: String? = null
		internal var location: String? = null
		internal var start: ZonedDateTime? = null
		internal var end: ZonedDateTime? = null

		fun title(title: String): Builder = apply { this.title = title }

		fun description(description: String) = apply { this.description = description }

		fun location(location: String): Builder = apply { this.location = location }

		fun start(start: ZonedDateTime): Builder = apply { this.start = start }

		fun end(end: ZonedDateTime): Builder = apply { this.end = end }

		fun build(): EventSpec
		{
			Objects.requireNonNull(title, "title = null")
			Objects.requireNonNull(start, "start = null")
			Objects.requireNonNull(end, "end = null")
			return EventSpec(this)
		}
	}

	fun getStartDate(): String
	{
		return start.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
	}

	fun getStartTime(): String
	{
		return start.format(DateTimeFormatter.ofPattern("hh:mm a"))
	}

	@JvmOverloads
	fun getEndFormatted(pattern: String = "MM-dd-yyyy"): String
	{
		return end.format(DateTimeFormatter.ofPattern(pattern))
	}

	fun getStartInstantEpoch(): Long
	{
		return start.toInstant().toEpochMilli()
	}

	fun getEndEpochMilli(): Long
	{
		return end.toInstant().toEpochMilli()
	}

	private fun formatLocalZone(time: ZonedDateTime): String
	{
		return "${time.zone.normalized()}:${time.year}${pad(time.month.value)}${pad(time.dayOfMonth)}T${pad(time.hour)}${pad(time.minute)}${pad(time.second)}"
	}

	private fun pad(i: Int): String
	{
		return when(i)
		{
			in 0..9 -> "0$i"
			else -> i.toString()
		}
	}

	fun text(): String
	{
		val sb = StringBuilder()
		sb.appendln("BEGIN:VEVENT")
		sb.appendln("UID:My_iCal_doevenceapp_2019-11-29104627.$id@uark.edu")
		sb.appendln("DTSTART;TZID=${formatLocalZone(start)}")
		sb.appendln("DTEND;TZID=${formatLocalZone(end)}")
		sb.appendln("DTSTAMP;TZID=${formatLocalZone(ZonedDateTime.now())}")
		sb.appendln("CLASS:PRIVATE")
		sb.appendln("CREATED:20191129T104627Z")
		sb.appendln("LAST-MODIFIED:20191129T104627Z")
		sb.appendln("SEQUENCE:0")
		sb.appendln("STATUS:CONFIRMED")
		sb.appendln("SUMMARY:$title")
		sb.appendln("DESCRIPTION:$description")
		sb.appendln("LOCATION:$location")
		sb.appendln("TRANSP:OPAQUE")
		sb.appendln("END:VEVENT")
		return sb.toString().trim()
	}

	override fun toString(): String {
		return "EventSpec(id=$id, title=$title, location=$location, start=$start, end=$end)"
	}
}