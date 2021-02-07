package projects.csce.evence

import daniel.perez.core.BaseActivity
import daniel.perez.core.DialogStarter
import daniel.perez.core.model.DateSetEvent
import daniel.perez.core.model.TimeSetEvent
import daniel.perez.core.model.ViewEvent
import daniel.perez.qrdialogview.CalendarDialog
import daniel.perez.qrdialogview.QRDialog
import io.reactivex.rxjava3.core.Observable

class DialogStarterImpl: DialogStarter
{
    override fun startQrDialog(activity: BaseActivity, data: ViewEvent)
    {
        QRDialog(activity, data)
    }

    override fun startTimeDialog(activity: BaseActivity): Observable<TimeSetEvent>
    {
        val calendarDialog = CalendarDialog(activity)
        return calendarDialog.timeDialog()
    }

    override fun startDateDialog(activity: BaseActivity): Observable<DateSetEvent>
    {
        val calendarDialog = CalendarDialog(activity)
        return calendarDialog.dateDialog()
    }
}