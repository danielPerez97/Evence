package daniel.perez.generateqrview

import androidx.lifecycle.ViewModel
import daniel.perez.core.db.EventOps
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.service.FileManager
import daniel.perez.core.service.qr.QrAttempt
import daniel.perez.core.service.qr.QrBitmapGenerator
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec
import io.reactivex.rxjava3.core.Flowable
import java.time.LocalDateTime
import javax.inject.Inject

class GenerateQrViewModel @Inject internal constructor(
        private val generator: QrBitmapGenerator,
        private val eventOps: EventOps,
        private val fileManager: FileManager) : ViewModel()
{

    fun saveEvent(event: ViewEvent)
    {
        eventOps.insertEvent(
                event.title,
                event.description,
                event.location,
                LocalDateTime.of(event.startYear(), event.startMonth(), event.startDayOfMonth(), event.startHour(), event.startMinute()),
                LocalDateTime.of(event.endYear(), event.endMonth(), event.endDayOfMonth(), event.endHour(), event.endMinute())
        )
    }

    fun saveFile(ical: ICalSpec?) {
        fileManager.saveICalFile(ical!!)
    }
}