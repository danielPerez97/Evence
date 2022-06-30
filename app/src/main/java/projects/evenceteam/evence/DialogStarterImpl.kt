package projects.evenceteam.evence

import coil.ImageLoader
import teamevence.evenceapp.core.ActivityStarter
import teamevence.evenceapp.core.BaseActivity
import teamevence.evenceapp.core.DialogStarter
import teamevence.evenceapp.core.db.EventOps
import teamevence.evenceapp.core.model.DateSetEvent
import teamevence.evenceapp.core.model.TimeSetEvent
import teamevence.evenceapp.core.model.ViewEvent
import teamevence.evenceapp.qrdialogview.CalendarDialog
import teamevence.evenceapp.qrdialogview.QRDialog
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

class DialogStarterImpl @Inject constructor(
        @Singleton val imageLoader: ImageLoader,
        @Singleton val activityStarter: ActivityStarter,
        @Singleton val eventOps: EventOps
): DialogStarter
{
    override fun startQrDialog(activity: BaseActivity, data: ViewEvent)
    {
        QRDialog(activity, data, imageLoader, activityStarter, eventOps)
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