package projects.csce.evence

import coil.ImageLoader
import daniel.perez.core.ActivityStarter
import daniel.perez.core.BaseActivity
import daniel.perez.core.DialogStarter
import daniel.perez.core.model.DateSetEvent
import daniel.perez.core.model.TimeSetEvent
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.service.FileManager
import daniel.perez.qrdialogview.CalendarDialog
import daniel.perez.qrdialogview.QRDialog
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

class DialogStarterImpl @Inject constructor(
        @Singleton val imageLoader: ImageLoader,
        @Singleton val activityStarter: ActivityStarter
): DialogStarter
{
    override fun startQrDialog(activity: BaseActivity, data: ViewEvent)
    {
        QRDialog(activity, data, imageLoader, activityStarter)
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