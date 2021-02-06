package daniel.perez.qrcameraview.viewmodel

import android.graphics.Rect
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.text.Text
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.service.FileManager
import daniel.perez.core.service.qr.QrBitmapGenerator
import daniel.perez.core.toZonedDateTime
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec
import daniel.perez.qrcameraview.Scanner.QRScanner
import daniel.perez.qrcameraview.Scanner.TextScanner
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class QrReaderViewModel @Inject constructor(
        private val fileManager: FileManager,
        private val generator: QrBitmapGenerator,
        private val QRScanner: QRScanner,
        private val textScanner: TextScanner): ViewModel() {

    fun saveFile(ical: ICalSpec) = fileManager.saveICalFile(ical)

    fun liveQRData(): Observable<MutableList<Barcode>> =  QRScanner.qrScannerResult()
    fun liveTextData(): Observable<Text> = textScanner.textScannerResult()
    fun liveQRBoundingBoxes() : Observable<List<Rect>> = QRScanner.qrBoundingBoxes()
    fun liveTextBoundingBoxes() : Observable<List<Rect?>> = textScanner.textBoundingBoxes()

    fun toViewEvent(event : EventSpec) : ViewEvent{
        val viewEvent = ViewEvent(event.title,
                event.description,
                event.getStartDate(),
                event.getStartTime(),
                event.getStartInstantEpoch(),
                event.getEndEpochMilli(),
                event.location,
                event.text(),
                generator.forceGenerate(event.text())
        )
        return viewEvent
    }

    fun toEventSpec(qr: Barcode) : EventSpec{
        val qrEvent = qr.calendarEvent
        val event = EventSpec.Builder(0)
                .title(qrEvent.summary)
                .description(qrEvent.description)
                .location(qrEvent.location)
                .start(toZonedDateTime(qrEvent.start.month, qrEvent.start.day, qrEvent.start.year, qrEvent.start.hours, qrEvent.start.minutes))
                .end(toZonedDateTime(qrEvent.end.month, qrEvent.end.day, qrEvent.end.year, qrEvent.end.hours, qrEvent.end.minutes))
                .build()
        return event
    }
}