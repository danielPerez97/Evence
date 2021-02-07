package projects.csce.evence.database

import daniel.perez.core.db.Event
import daniel.perez.core.db.EventOps
import daniel.perez.evencedb.EventQueries
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

internal class EventOpsImpl( private val queries: EventQueries): EventOps
{
    override fun insertEvent(title: String, description: String, location: String, startTime: LocalDateTime, endTime: LocalDateTime)
    {
        return queries.insertEvent(
                title,
                description,
                location,
                startTime.toKotlinLocalDateTime(),
                endTime.toKotlinLocalDateTime()
        )
    }

    override fun insertEventWithRecurrence(title: String, description: String, location: String, startTime: LocalDateTime, endTime: LocalDateTime, recurrenceRule: String)
    {
        return queries.insertEventWithRecurrence(
                title,
                description,
                location,
                startTime.toKotlinLocalDateTime(),
                endTime.toKotlinLocalDateTime(),
                recurrenceRule
        )
    }

    override fun selectAll(): List<Event>
    {
        return queries.selectAll().executeAsList().map { it.convert() }
    }

    override fun selectByTitle(title: String): Event
    {
        return queries.selectByTitle(title).executeAsOne().convert()
    }

    override fun getEventById(id: Long): Event
    {
        return queries.getEventById(id).executeAsOne().convert()
    }

    override fun getEventsSortedSoonest(): List<Event>
    {
        return queries.getEventsSortedSoonest().executeAsList().map { it.convert() }
    }

    override fun getEventsSortedLatest(): List<Event>
    {
        return queries.getEventsSortedLatest().executeAsList().map { it.convert() }
    }

    private fun daniel.perez.evencedb.Event.convert(): Event
    {
        return Event(title, description, location, start_time.toJavaLocalDateTime(), end_time.toJavaLocalDateTime(), recurrence_rule)
    }
}

fun getEventOps( queries: EventQueries ): EventOps = EventOpsImpl( queries )