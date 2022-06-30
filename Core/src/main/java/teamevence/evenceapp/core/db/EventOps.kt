package teamevence.evenceapp.core.db

import teamevence.evenceapp.core.model.ViewEvent
import io.reactivex.rxjava3.core.Observable
import java.time.LocalDateTime

interface EventOps
{
    fun insertEvent(event: UiNewEvent): Observable<Long>

    fun insertEvent(title: String, description: String, location: String, startTime: LocalDateTime, endTime: LocalDateTime): Observable<Long>

    fun insertEventWithRecurrence(title: String, description: String, location: String, startTime: LocalDateTime, endTime: LocalDateTime, recurrenceRule: String)

    fun updateEvent(id: Long, event: UiNewEvent): Observable<Long>

    fun updateEvent(id: Long, title: String, description: String, location: String, startTime: LocalDateTime, endTime: LocalDateTime): Observable<Long>

    fun selectAll(): Observable<List<Event>>

    fun selectAllByDateAscending(): Observable<List<Event>>

    fun selectAllByRecentlyCreated(): Observable<List<Event>>

    fun searchByTitle(title: String): Observable<List<Event>>

    fun getEventById(id: Long): Observable<Event>

    fun getEventsSortedSoonest(): Observable<List<Event>>

    fun getEventsSortedLatest(): Observable<List<Event>>

    fun icsText(event: Event): Observable<String>

    fun icsText(event: ViewEvent): Observable<String>

    fun deleteById(id: Long)
}