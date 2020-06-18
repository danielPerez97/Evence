package daniel.perez.qrcameraview.viewmodel

import androidx.lifecycle.ViewModel
import daniel.perez.core.service.FileManager
import daniel.perez.ical.ICalSpec
import javax.inject.Inject

class QrReaderViewModel @Inject constructor(private val fileManager: FileManager): ViewModel() {
    fun saveFile(ical: ICalSpec) {
        fileManager.saveICalFile(ical)
    }
}