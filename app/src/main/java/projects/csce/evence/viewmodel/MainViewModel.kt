package projects.csce.evence.viewmodel

import androidx.lifecycle.ViewModel
import daniel.perez.core.db.Event
import daniel.perez.core.db.EventOps
import daniel.perez.core.db.toViewEvent
import daniel.perez.core.model.ViewEvent
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class MainViewModel @Inject internal constructor(
        private val eventOps: EventOps
) : ViewModel()
{
    fun liveFiles(): Observable<List<ViewEvent>>
    {
        return eventOps.selectAll().map { it.toViewEvent() }
    }

    private fun List<Event>.toViewEvent(): List<ViewEvent>
    {
        return this.map { it.toViewEvent() }
    }
}