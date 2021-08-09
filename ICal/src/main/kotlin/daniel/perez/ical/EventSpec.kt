package daniel.perez.ical

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Suppress("MemberVisibilityCanBePrivate")
class EventSpec private constructor(builder: Builder) {
	val id: Int = builder.id
	val title: String = builder.title!!
	val description = builder.description ?: ""
	val dtstamp: ZonedDateTime = builder.dtstamp
	val location: String = builder.location ?: ""
	val start: ZonedDateTime = builder.start!!
	val end: ZonedDateTime = builder.end!!

	data class Builder constructor(internal val id: Int) {
		internal var title: String? = null
		internal var description: String? = null
		internal var dtstamp: ZonedDateTime = ZonedDateTime.now()
		internal var location: String? = null
		internal var start: ZonedDateTime? = null
		internal var end: ZonedDateTime? = null

		fun title(title: String): Builder = apply { this.title = title }

		fun description(description: String) = apply { this.description = description }

		fun dtstamp(dtstamp: ZonedDateTime) = apply { this.dtstamp = dtstamp }

		fun location(location: String): Builder = apply { this.location = location }

		fun start(start: ZonedDateTime): Builder = apply { this.start = start }

		fun end(end: ZonedDateTime): Builder = apply { this.end = end }

		fun build(): EventSpec {
			Objects.requireNonNull(title, "title = null")
			Objects.requireNonNull(start, "start = null")
			Objects.requireNonNull(end, "end = null")
			return EventSpec(this)
		}
	}

	fun getStartDate(): String {
		return start.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
	}

	fun getStartTime(): String {
		return start.format(DateTimeFormatter.ofPattern("hh:mm a"))
	}

	@JvmOverloads
	fun getEndFormatted(pattern: String = "MM-dd-yyyy"): String {
		return end.format(DateTimeFormatter.ofPattern(pattern))
	}

	fun getStartInstantEpoch(): Long {
		return start.toInstant().toEpochMilli()
	}

	fun getEndEpochMilli(): Long {
		return end.toInstant().toEpochMilli()
	}

	private fun formatLocalZone(time: ZonedDateTime): String {
		return "${time.zone.normalized()}:${time.year}${pad(time.month.value)}${pad(time.dayOfMonth)}T${pad(time.hour)}${pad(time.minute)}${pad(time.second)}"
	}

	private fun pad(i: Int): String {
		return when (i) {
			in 0..9 -> "0$i"
			else -> i.toString()
		}
	}

	fun text(): String {
		val sb = StringBuilder()
		sb.appendLine("BEGIN:VEVENT")
		sb.appendLine("UID:My_iCal_doperez_2019-11-29104627.$id@uark.edu")
		sb.appendLine("DTSTART;TZID=${formatLocalZone(start)}")
		sb.appendLine("DTEND;TZID=${formatLocalZone(end)}")
		sb.appendLine("DTSTAMP;TZID=${formatLocalZone(dtstamp)}")
		sb.appendLine("CLASS:PRIVATE")
		sb.appendLine("CREATED:20191129T104627Z")
		sb.appendLine("LAST-MODIFIED:20191129T104627Z")
		sb.appendLine("SEQUENCE:0")
		sb.appendLine("STATUS:CONFIRMED")
		sb.appendLine("SUMMARY:$title")
		sb.appendLine("DESCRIPTION:$description")
		sb.appendLine("LOCATION:$location")
		sb.appendLine("TRANSP:OPAQUE")
		sb.appendLine("END:VEVENT")
		return sb.toString().trim()
	}

	override fun toString(): String {
		return "EventSpec(id=$id, title=$title, location=$location, start=$start, end=$end)"
	}
}