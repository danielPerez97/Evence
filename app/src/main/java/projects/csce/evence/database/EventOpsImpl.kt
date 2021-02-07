package projects.csce.evence.database

import com.squareup.sqldelight.runtime.rx3.asObservable
import com.squareup.sqldelight.runtime.rx3.mapToList
import com.squareup.sqldelight.runtime.rx3.mapToOne
import daniel.perez.core.db.Event
import daniel.perez.core.db.EventOps
import daniel.perez.evencedb.EventQueries
import io.reactivex.rxjava3.core.Observable
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

    override fun selectAll(): Observable<List<Event>>
    {
        return queries.selectAll()
                .asObservable()
                .mapToList()
                .map { it.convert() }
    }

    override fun selectByTitle(title: String): Observable<Event>
    {
        return queries.selectByTitle(title)
                .asObservable()
                .mapToOne()
                .map { it.convert() }
    }

    override fun getEventById(id: Long): Observable<Event>
    {
        return queries.getEventById(id)
                .asObservable()
                .mapToOne()
                .map { it.convert() }
    }

    override fun getEventsSortedSoonest(): Observable<List<Event>>
    {
        return queries.getEventsSortedSoonest()
                .asObservable()
                .mapToList()
                .map { it.convert() }
    }

    override fun getEventsSortedLatest(): Observable<List<Event>>
    {
        return queries.getEventsSortedLatest()
                .asObservable()
                .mapToList()
                .map { it.convert() }
    }

    private fun daniel.perez.evencedb.Event.convert(): Event
    {
        return Event(_id, title, description, location, start_time.toJavaLocalDateTime(), end_time.toJavaLocalDateTime(), recurrence_rule)
    }

    private fun List<daniel.perez.evencedb.Event>.convert(): List<Event>
    {
        return this.map { it.convert() }
    }
}

fun getEventOps( queries: EventQueries ): EventOps = EventOpsImpl( queries )