package teamevence.evenceapp.qrcameraview.viewmodel

//import com.google.mlkit.vision.text.Text
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.Barcode
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import teamevence.evenceapp.core.db.Event
import teamevence.evenceapp.core.db.EventOps
import teamevence.evenceapp.core.db.UiNewEvent
import teamevence.evenceapp.core.toLocalDateTime
import teamevence.evenceapp.qrcameraview.Scanner.QRScanner
import teamevence.evenceapp.qrcameraview.Scanner.TextScanner
import teamevence.evenceapp.qrcameraview.data.SCAN_TYPE
import teamevence.evenceapp.qrcameraview.data.ScannedData
import teamevence.evenceapp.qrcameraview.data.ScannedQR
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class QrReaderViewModel @Inject constructor(
    private val qrScanner: QRScanner,
    private val textScanner: TextScanner,
    private val eventOps: EventOps
) : ViewModel() {

    fun liveQRData(): Observable<List<ScannedData>> {
        return qrScanner.qrScannerResult()
            .doOnSubscribe { Timber.d("Subscribed to qrScannerResult") }
//                .flatMap { barcodes: List<Barcode> ->
//                    Observable.just(barcodes)
//                            .flatMapIterable { barcode: List<Barcode> -> barcodes }
//                            .map { barcode: Barcode ->
//                                ScannedQR(SCAN_TYPE.BARCODE, barcode)
//                            }
//                            .toList()
//                            .toObservable()
//                }
            .doOnNext { Timber.d("Received onNext event") }
            .map {
                it.map { ScannedQR(SCAN_TYPE.BARCODE, it) }
            }
    }

    fun toUINewEvent(event: Barcode.CalendarEvent): UiNewEvent {
        with(event) {
            val startDate =
                toLocalDateTime(start.day, start.month, start.year, start.hours, start.minutes)

            lateinit var endDate: LocalDateTime
            if (end == null) endDate == startDate
            else {
                val endDay = if (end.day <= 0) start.day else end.day
                val endMonth = if (end.month <= 0) start.month else end.month
                val endYear = if (end.year <= 0) start.year else end.year
                endDate = toLocalDateTime(endDay, endMonth, endYear, end.hours, end.minutes)
            }

            var uiNewEvent = UiNewEvent(
                summary.ifEmpty { "Untitled" },
                description,
                location,
                startDate,
                endDate
            )
            return uiNewEvent
        }
    }


/* FOR FUTURE UPDATES
    fun liveTextData(): Observable<List<ScannedData>> {
        return textScanner.textBlockResult()
                .flatMap { texts: List<Text.TextBlock> ->
                    Observable.just(texts)
                            .flatMapIterable { text: List<Text.TextBlock> -> texts }
                            .map { text: Text.TextBlock ->
                                ScannedText(SCAN_TYPE.TEXT, text)
                            }
                            .toList()
                            .toObservable()
                }
    }*/

    fun saveEvent(event: UiNewEvent): Observable<Event> {
        return eventOps.insertEvent(event)
            .flatMap { eventOps.getEventById(it) }
    }

}