package teamevence.evenceapp.qrcameraview.viewmodel

import androidx.lifecycle.ViewModel
//import com.google.mlkit.vision.text.Text
import dagger.hilt.android.lifecycle.HiltViewModel
import teamevence.evenceapp.core.db.Event
import teamevence.evenceapp.core.db.EventOps
import teamevence.evenceapp.core.db.UiNewEvent
import teamevence.evenceapp.qrcameraview.Scanner.QRScanner
import teamevence.evenceapp.qrcameraview.Scanner.TextScanner
import teamevence.evenceapp.qrcameraview.data.SCAN_TYPE
import teamevence.evenceapp.qrcameraview.data.ScannedData
import teamevence.evenceapp.qrcameraview.data.ScannedQR
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QrReaderViewModel @Inject constructor(
        private val qrScanner: QRScanner,
        private val textScanner: TextScanner,
        private val eventOps: EventOps): ViewModel() {

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
            .doOnNext{ Timber.d("Receieved onNext event") }
            .map {
                it.map { ScannedQR(SCAN_TYPE.BARCODE, it) }
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

    fun saveEvent(event: UiNewEvent): Observable<Event>
    {
        return eventOps.insertEvent( event )
                .flatMap { eventOps.getEventById(it) }
    }

}