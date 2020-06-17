package projects.csce.evence.viewmodel

import androidx.lifecycle.ViewModel
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec
import projects.csce.evence.service.model.FileManager
import javax.inject.Inject

class QrReaderViewModel @Inject constructor(private val fileManager: FileManager): ViewModel() {
    fun saveFile(ical: ICalSpec) {
        fileManager.saveICalFile(ical)
    }
}