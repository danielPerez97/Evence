package projects.csce.evence.service.model.event

import java.util.Objects

class Event private constructor(builder: Builder)
{
	val title: String = builder.title
	val startDate: String = builder.startDate
	val endDate: String = builder.endDate
	val location: String = builder.location
	val description: String = builder.description

	class Builder constructor()
	{
		internal var title: String = ""
		internal var startDate: String = ""
		internal var endDate: String = ""
		internal var location: String = ""
		internal var description: String = ""

		internal constructor(event: Event): this()
		{
			this.title = event.title
			this.startDate = event.startDate
			this.endDate = event.endDate
			this.location = event.location
			this.description = event.description
		}
		fun title(title: String): Builder = apply {
			Objects.requireNonNull(title, "title == null")
			this.title = title
			return this
		}

		fun startDate(startDate: String): Builder = apply {
			Objects.requireNonNull(startDate, "startDate == null")
			this.startDate = startDate
			return this
		}

		fun endDate(endDate: String): Builder = apply { this.endDate = endDate }

		fun location(location: String): Builder = apply { this.location = location }

		fun description(description: String): Builder = apply { this.description = description }

		fun build(): Event = Event(this)
	}

	fun newBuilder(): Builder = Builder(this)

	fun text(): String
	{
		return """
			BEGIN:VEVENT
			SUMMARY:$title
			DTSTART:$startDate
			DTEND:$endDate
			LOCATION:$location
			DESCRIPTION:$description
			END:VEVENT
		""".trimIndent()
	}
}