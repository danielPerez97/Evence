package projects.csce.evence

import android.content.Intent
import daniel.perez.core.ActionResult
import daniel.perez.core.ActivityResultActions
import daniel.perez.core.BaseActivity
import daniel.perez.core.db.EventOps
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.service.FileManager
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