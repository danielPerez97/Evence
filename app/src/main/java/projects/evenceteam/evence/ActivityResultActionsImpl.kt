package projects.evenceteam.evence

import android.content.Intent
import teamevence.evenceapp.core.ActionResult
import teamevence.evenceapp.core.ActivityResultActions
import teamevence.evenceapp.core.BaseActivity
import teamevence.evenceapp.core.db.EventOps
import teamevence.evenceapp.core.model.ViewEvent
import teamevence.evenceapp.core.service.FileManager
import io.reactivex.rxjava3.core.Observable

class ActivityResultActionsImpl(
        private val eventOps: EventOps,
        private val fileManager: FileManager
): ActivityResultActions
{
    override fun actionCreateDocumentEvent(context: BaseActivity, viewEvent: ViewEvent, data: Intent): Observable<ActionResult>
    {
        return eventOps.getEventById( viewEvent.id )
                .flatMap { event ->
                    Observable.just(event)
                            .map<ActionResult> {
                                fileManager.writeFileActionCreateDocument(context, it, data)
                                ActionResult.Success
                            }
                            .onErrorReturn { ActionResult.Failure(it) }
                            .startWithItem(ActionResult.InTransit)
                }
    }
}