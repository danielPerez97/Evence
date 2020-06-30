package daniel.perez.qrcameraview.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import daniel.perez.core.service.FileManager
import daniel.perez.ical.ICalSpec
import javax.inject.Inject

class QrReaderViewModel @Inject constructor(private val fileManager: FileManager): ViewModel() {
    fun saveFile(ical: ICalSpec) {
        fileManager.saveICalFile(ical)
    }


}