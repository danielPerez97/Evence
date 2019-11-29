package projects.csce.evence.ical

import org.threeten.bp.ZonedDateTime
import java.util.Objects


class EventSpec private constructor(builder: Builder)
{
	val id: Int = builder.id
	val title: String = builder.title!!
	val start: ZonedDateTime = builder.start!!
	val end: ZonedDateTime = builder.end!!

	class Builder internal constructor(internal val id: Int)
	{
		internal var title: String? = null
		internal var start: ZonedDateTime? = null
		internal var end: ZonedDateTime? = null

		fun title(title: String): Builder = apply { this.title = title }

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

	companion object
	{
		@JvmStatic fun builder(id: Int): Builder = Builder(id)
	}

	private fun formatLocalZone(time: ZonedDateTime): String
	{
		return "${time.zone.normalized()}:${time.year}${pad(time.month.value)}${time.dayOfMonth}T${pad(time.hour)}${pad(time.minute)}${pad(time.second)}"
	}

	private fun pad(i: Int): String
	{
		return when(i)
		{
			in 0..9 -> "0$i"
			else -> i.toString()
		}
	}

	internal fun text(): String
	{
		val sb = StringBuilder()
		sb.appendln("BEGIN:VEVENT")
		sb.appendln("DTSTART;TZID=${formatLocalZone(start)}")
		sb.appendln("DTEND;TZID=${formatLocalZone(end)}")
		sb.appendln("DTSTAMP;TZID=${formatLocalZone(ZonedDateTime.now())}")
		sb.appendln("UID:My_iCal_doperez_2019-11-29104627.$id@uark.edu")
		sb.appendln("CLASS:PRIVATE")
		sb.appendln("CREATED:20191129T104627Z")
		sb.appendln("LAST-MODIFIED:20191129T104627Z")
		sb.appendln("SEQUENCE:0")
		sb.appendln("STATUS:CONFIRMED")
		sb.appendln("SUMMARY:$title")
		sb.appendln("TRANSP:OPAQUE")
		sb.appendln("END:VEVENT")
		return sb.toString().trim()
	}
}