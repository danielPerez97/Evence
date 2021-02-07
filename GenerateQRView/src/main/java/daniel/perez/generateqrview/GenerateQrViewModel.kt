package daniel.perez.generateqrview

import androidx.lifecycle.ViewModel
import daniel.perez.core.db.EventOps
import daniel.perez.core.service.FileManager
import daniel.perez.core.service.qr.QrAttempt
import daniel.perez.core.service.qr.QrBitmapGenerator
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class GenerateQrViewModel @Inject internal constructor(
        private val generator: QrBitmapGenerator,
        private val eventOps: EventOps,
        private val fileManager: FileManager) : ViewModel()
{
    fun generateQrBitmap(event: EventSpec?) {
        generator.generate(event!!)
    }

    fun qrImages(): Flowable<QrAttempt> {
        return generator.generations()
    }

    fun saveEvent(event: EventSpec)
    {
        eventOps.insertEvent(
                event.title,
                event.description,
                event.location,
                event.start.toLocalDateTime(),
                event.end.toLocalDateTime()
        )
    }

    fun saveFile(ical: ICalSpec?) {
        fileManager.saveICalFile(ical!!)
    }
}