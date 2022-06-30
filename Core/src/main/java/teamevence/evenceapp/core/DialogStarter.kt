package teamevence.evenceapp.core

import teamevence.evenceapp.core.model.DateSetEvent
import teamevence.evenceapp.core.model.TimeSetEvent
import teamevence.evenceapp.core.model.ViewEvent
import io.reactivex.rxjava3.core.Observable

interface DialogStarter
{
    fun startQrDialog(activity: BaseActivity, data: ViewEvent)
    fun startTimeDialog(activity: BaseActivity): Observable<TimeSetEvent>
    fun startDateDialog(activity: BaseActivity): Observable<DateSetEvent>
}