package daniel.perez.generateqrview

import androidx.lifecycle.ViewModel
import daniel.perez.core.db.Event
import daniel.perez.core.db.EventOps
import daniel.perez.core.db.UiNewEvent
import daniel.perez.core.db.toViewEvent
import daniel.perez.core.model.ViewEvent
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GenerateQrViewModel @Inject internal constructor(private val eventOps: EventOps) : ViewModel()
{
    fun saveEvent(event: UiNewEvent): Observable<Event>
    {
        return eventOps.insertEvent( event )
                .flatMap { eventOps.getEventById(it) }
    }
}