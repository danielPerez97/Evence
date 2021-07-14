package daniel.perez.generateqrview

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.perez.core.db.Event
import daniel.perez.core.db.EventOps
import daniel.perez.core.db.UiNewEvent
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

@HiltViewModel
class GenerateQrViewModel @Inject internal constructor(private val eventOps: EventOps) : ViewModel()
{
    fun saveEvent(event: UiNewEvent): Observable<Event>
    {
        return eventOps.insertEvent( event )
                .flatMap {
                    // Take one so it completes the Observable. Allows you to hit the delete button
                    // and not error out because this Observable tries to update the affected row.
                    eventOps.getEventById(it)
                            .take(1)
                }
    }
}