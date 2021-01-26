package daniel.perez.core

import daniel.perez.core.model.DateSetEvent
import daniel.perez.core.model.TimeSetEvent
import daniel.perez.core.model.ViewCalendarData
import io.reactivex.rxjava3.core.Observable

interface DialogStarter
{
    fun startQrDialog(activity: BaseActivity, data: ViewCalendarData)
    fun startTimeDialog(activity: BaseActivity): Observable<TimeSetEvent>
    fun startDateDialog(activity: BaseActivity): Observable<DateSetEvent>
}