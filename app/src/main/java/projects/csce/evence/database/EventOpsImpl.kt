package projects.csce.evence.database

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.squareup.sqldelight.runtime.rx3.asObservable
import com.squareup.sqldelight.runtime.rx3.mapToList
import com.squareup.sqldelight.runtime.rx3.mapToOne
import daniel.perez.core.db.Event
import daniel.perez.core.db.EventOps
import daniel.perez.core.db.UiNewEvent
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.service.FileManager
import daniel.perez.evencedb.EventQueries
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

internal fun Context.getEventOps( queries: EventQueries, fileManager: FileManager, timeZone: TimeZone = TimeZone.getDefault() ): EventOps = EventOpsImpl(  this, queries, fileManager, timeZone.toZoneId() )

private class EventOpsImpl(
        private val appContext: Context,
        private val queries: EventQueries,
        private val fileManager: FileManager,
        private val timeZone: ZoneId): EventOps
{
    override fun insertEvent(event: UiNewEvent): Observable<Long>
    {
        return insertEvent(event.title, event.description, event.location, event.start, event.end)
    }

    override fun insertEvent(title: String, description: String, location: String, startTime: LocalDateTime, endTime: LocalDateTime): Observable<Long>
    {
        return queries.transactionWithResult {
            queries.insertEvent(
                    title,
                    description,
                    location,
                    startTime.toKotlinLocalDateTime(),
                    endTime.toKotlinLocalDateTime()
            )
            queries.lastInsertRowID().asObservable().mapToOne()
        }
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
                .map { it.toEvent() }
    }

    override fun searchByTitle(title: String): Observable<List<Event>>
    {
        return queries.selectByTitle( title )
                .asObservable()
                .mapToList()
                .map { it.toEvent() }
    }

    override fun getEventById(id: Long): Observable<Event>
    {
        return queries.getEventById(id)
                .asObservable()
                .mapToOne()
                .map { it.toEvent() }
    }

    override fun getEventsSortedSoonest(): Observable<List<Event>>
    {
        return queries.getEventsSortedSoonest()
                .asObservable()
                .mapToList()
                .map { it.toEvent() }
    }

    override fun getEventsSortedLatest(): Observable<List<Event>>
    {
        return queries.getEventsSortedLatest()
                .asObservable()
                .mapToList()
                .map { it.toEvent() }
    }

    override fun icsText(event: Event): Observable<String>
    {
        return queries.getEventById( event.id )
                .asObservable()
                .mapToOne()
                .map { it.toICalSpec().text() }
    }

    override fun icsText(event: ViewEvent): Observable<String>
    {
        return queries.getEventById( event.id )
                .asObservable()
                .mapToOne()
                .map { it.toICalSpec().text() }
    }

    override fun deleteById(id: Long): Completable
    {
        return Completable.fromAction { queries.deleteById(id) }
    }

    private fun daniel.perez.evencedb.Event.toEvent(): Event
    {
        val imageFile = fileManager.getOrSaveImage( this.toICalSpec(), this._id.toString() )
        return Event(
                _id,
                title,
                description,
                location,
                start_time.toJavaLocalDateTime(),
                end_time.toJavaLocalDateTime(),
                getImageFileUri( imageFile ),
                getImageContentUri( imageFile ),
                getIcsUri(this, _id),
                recurrence_rule
        )
    }

    private fun getImageContentUri(imageFile: File): Uri
    {
        return FileProvider.getUriForFile(appContext, "projects.csce.evence.fileprovider", imageFile)
    }

    private fun getImageFileUri( imageFile: File): Uri
    {
        return Uri.fromFile( imageFile )
    }

    private fun getIcsUri(event: daniel.perez.evencedb.Event, id: Long): Uri
    {
        return Uri.fromFile( fileManager.getOrSaveIcs( event.toICalSpec(), id.toString() ) )
    }

    private fun daniel.perez.evencedb.Event.toICalSpec(): ICalSpec
    {
        return ICalSpec.Builder()
                .addEvent(
                        EventSpec.Builder( this._id.toInt() )
                                .title( this.title )
                                .description( this.description )
                                .location( this.location )
                                .start( this.start_time.toJavaLocalDateTime().atZone( timeZone ) )
                                .end( this.end_time.toJavaLocalDateTime().atZone( timeZone ) )
                                .build()
                )
                .build()
    }

    private fun List<daniel.perez.evencedb.Event>.toEvent(): List<Event>
    {
        return this.map { it.toEvent() }
    }
}