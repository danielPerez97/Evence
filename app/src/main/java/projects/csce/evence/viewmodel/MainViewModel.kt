package projects.csce.evence.viewmodel

import androidx.lifecycle.ViewModel
import daniel.perez.core.db.Event
import daniel.perez.core.db.EventOps
import daniel.perez.core.db.dateString
import daniel.perez.core.db.timeString
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.service.FileManager
import daniel.perez.core.service.qr.QrBitmapGenerator
import daniel.perez.ical.EventSpec
import io.reactivex.rxjava3.core.Observable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.ZoneOffset
import java.util.stream.Collectors
import javax.inject.Inject

class MainViewModel @Inject internal constructor(
        private val eventOps: EventOps
        ) : ViewModel()
{
    fun liveFiles(): Observable<List<ViewEvent>>
    {
        return eventOps.selectAll().map { it.toViewEvent() }
    }

    private fun List<Event>.toViewEvent(): List<ViewEvent>
    {
        return this.map {
            ViewEvent(
                    it.id,
                    it.title,
                    it.description,
                    it.location,
                    it.startTime,
                    it.endTime,
                    it.qrImageUri
            )
        }


    }
}