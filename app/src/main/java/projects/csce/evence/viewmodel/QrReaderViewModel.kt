package projects.csce.evence.viewmodel

import androidx.lifecycle.ViewModel
import projects.csce.evence.ical.ICalSpec
import projects.csce.evence.service.model.FileManager
import javax.inject.Inject

class QrReaderViewModel @Inject constructor(private val fileManager: FileManager): ViewModel() {
    fun saveFile(ical: ICalSpec) {
        fileManager.saveICalFile(ical)
    }
}