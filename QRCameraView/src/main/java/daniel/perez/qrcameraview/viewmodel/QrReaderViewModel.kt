package daniel.perez.qrcameraview.viewmodel

import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.text.Text
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.perez.core.db.Event
import daniel.perez.core.db.EventOps
import daniel.perez.core.db.UiNewEvent
import daniel.perez.qrcameraview.Scanner.QRScanner
import daniel.perez.qrcameraview.Scanner.TextScanner
import daniel.perez.qrcameraview.data.SCAN_TYPE
import daniel.perez.qrcameraview.data.ScannedData
import daniel.perez.qrcameraview.data.ScannedQR
import daniel.perez.qrcameraview.data.ScannedText
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
    }

    fun saveEvent(event: UiNewEvent): Observable<Event>
    {
        return eventOps.insertEvent( event )
                .flatMap { eventOps.getEventById(it) }
    }

}