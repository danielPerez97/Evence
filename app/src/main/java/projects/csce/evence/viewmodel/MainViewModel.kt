package projects.csce.evence.viewmodel

import androidx.lifecycle.ViewModel
import daniel.perez.core.db.Event
import daniel.perez.core.db.EventOps
import daniel.perez.core.db.toViewEvent
import daniel.perez.core.model.ViewEvent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject internal constructor(
        private val eventOps: EventOps
) : ViewModel()
{
    private val searchTextSubject: PublishSubject<String> = PublishSubject.create()

    fun events(): Observable<List<ViewEvent>>
    {
        return searchTextSubject.doOnNext { Timber.i("""Querying '$it'""") }
                .switchMap { title: String ->
                    if(title.isNotEmpty())
                    {
                        return@switchMap eventOps.searchByTitle(title).map { it.toViewEvents() }
                    }
                    else
                    {
                        return@switchMap eventOps.selectAll().map { it.toViewEvents() }
                    }
                }
    }

    fun newSearch( title: String )
    {
        Timber.i("New Search: $title")
        searchTextSubject.onNext(title)
    }

    private fun List<Event>.toViewEvents(): List<ViewEvent>
    {
        return this.map { it.toViewEvent() }
    }
}