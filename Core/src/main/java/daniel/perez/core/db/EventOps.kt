package daniel.perez.core.db

import java.time.LocalDateTime

interface EventOps
{
    fun insertEvent(title: String, description: String, location: String, startTime: LocalDateTime, endTime: LocalDateTime)

    fun insertEventWithRecurrence(title: String, description: String, location: String, startTime: LocalDateTime, endTime: LocalDateTime, recurrenceRule: String)

    fun selectAll(): List<Event>

    fun selectByTitle(title: String): Event

    fun getEventById(id: Long): Event

    fun getEventsSortedSoonest(): List<Event>

    fun getEventsSortedLatest(): List<Event>
}