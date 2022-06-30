package projects.evenceteam.evence.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import teamevence.evenceapp.core.db.Event
import teamevence.evenceapp.core.db.EventOps
import teamevence.evenceapp.core.db.toViewEvent
import teamevence.evenceapp.core.model.ViewEvent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class MainViewModel @Inject constructor(
        val handle: SavedStateHandle,
        @Singleton private val eventOps: EventOps
) : ViewModel()
{
    private val searchTextSubject: PublishSubject<QueryAndSortState> = PublishSubject.create()
    private var queryText: String = ""
    private var sortState: SortState = SortState.BY_DATE_ASCENDING

    fun events(): Observable<List<ViewEvent>>
    {
        return searchTextSubject.doOnNext { Timber.i("""Querying '$it'""") }
                .switchMap { state: QueryAndSortState ->
                    if(state.title.isNotEmpty())
                    {
                        return@switchMap eventOps.searchByTitle(state.title).map { it.toViewEvents() }
                    }
                    else
                    {
                        return@switchMap when(state.sortState)
                        {
                            SortState.BY_DATE_ASCENDING -> eventOps.selectAllByDateAscending().map { it.toViewEvents() }
                            SortState.RECENTLY_CREATED -> eventOps.selectAllByRecentlyCreated().map { it.toViewEvents() }
                        }
                    }
                }
    }

    fun newSearch( title: String )
    {
        Timber.i("New Search: $title")
        queryText = title
        searchTextSubject.onNext( QueryAndSortState(this.queryText, sortState) )
    }

    fun setSortState( sortState: SortState )
    {
        this.sortState = sortState
        searchTextSubject.onNext( QueryAndSortState(queryText, this.sortState) )
    }

    private fun List<Event>.toViewEvents(): List<ViewEvent>
    {
        return this.map { it.toViewEvent() }
    }

    private data class QueryAndSortState(
            val title: String,
            val sortState: SortState
    )
}