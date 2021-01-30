package daniel.perez.qrcameraview.viewmodel

//import com.google.android.gms.tasks.Task
//import com.google.mlkit.vision.barcode.Barcode
//import com.google.mlkit.vision.barcode.BarcodeScannerOptions
//import com.google.mlkit.vision.barcode.BarcodeScanning
//import com.google.mlkit.vision.common.InputImage
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.Barcode
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.service.FileManager
import daniel.perez.core.service.qr.QrBitmapGenerator
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec
import daniel.perez.qrcameraview.QrHandler
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class QrReaderViewModel @Inject constructor(private val fileManager: FileManager, private val qrHandler: QrHandler, private val generator: QrBitmapGenerator): ViewModel() {
    fun saveFile(ical: ICalSpec) {
        fileManager.saveICalFile(ical)
    }

    fun liveQRData(): Observable<Barcode> {
        return qrHandler.qrResult()
    }

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

}