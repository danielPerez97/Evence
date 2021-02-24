package daniel.perez.core

import android.content.Intent
import daniel.perez.core.model.ViewEvent
import io.reactivex.rxjava3.core.Observable

interface ActivityResultActions
{
    fun actionCreateDocumentEvent(context: BaseActivity, viewEvent: ViewEvent, data: Intent): Observable<ActionResult>
}