package projects.csce.evence.viewmodel

import androidx.lifecycle.ViewModel
import daniel.perez.core.model.ViewCalendarData
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.service.FileManager
import daniel.perez.core.service.qr.QrBitmapGenerator
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec
import io.reactivex.Observable
import java.util.stream.Collectors
import javax.inject.Inject

class MainViewModel @Inject internal constructor(private val fileManager: FileManager, private val generator: QrBitmapGenerator) : ViewModel()
{
    fun liveFiles(): Observable<List<ViewCalendarData>>
    {
        return fileManager.icals()
                .flatMap { iCalSpecs: List<ICalSpec> ->
                    Observable.just(iCalSpecs)
                            .flatMapIterable { iCalSpec: List<ICalSpec>? -> iCalSpecs }
                            .map { iCalSpec: ICalSpec ->
                                val events = map(iCalSpec.events)
                                ViewCalendarData(iCalSpec.fileName, events)
                            }
                            .toList()
                            .toObservable()
                }
    }

    private fun map(eventSpecs: List<EventSpec>): List<ViewEvent>
    {
        return eventSpecs.stream()
                .map { event: EventSpec ->
                    ViewEvent(
                            event.title,
                            event.location,
                            event.getStartDate(),
                            event.getStartTime(),
                            event.getStartInstantEpoch(),
                            event.getEndEpochMilli(),
                            event.location,
                            event.text(),
                            generator.forceGenerate(event.text())
                    )
                }
                .collect(Collectors.toList())
    }
}