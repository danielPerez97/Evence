package daniel.perez.qrcameraview.viewmodel

import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.Barcode
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.service.FileManager
import daniel.perez.core.service.qr.QrBitmapGenerator
import daniel.perez.core.toZonedDateTime
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec
import daniel.perez.qrcameraview.QrHandler
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class QrReaderViewModel @Inject constructor(private val fileManager: FileManager): ViewModel() {

}