package projects.csce.evence

import android.widget.TextView
import daniel.perez.core.BaseActivity
import daniel.perez.core.DialogStarter
import daniel.perez.core.model.ViewCalendarData
import daniel.perez.qrdialogview.CalendarDialog
import daniel.perez.qrdialogview.QRDialog

class DialogStarterImpl: DialogStarter
{
    override fun startQrDialog(activity: BaseActivity, data: ViewCalendarData)
    {
        QRDialog(activity, data)
    }

    override fun startTimeDialog(activity: BaseActivity, textView: TextView)
    {
        val calendarDialog = CalendarDialog(activity, textView)
        calendarDialog.timeDialog()
    }

    override fun startDateDialog(activity: BaseActivity, textView: TextView)
    {
        val calendarDialog = CalendarDialog(activity, textView)
        calendarDialog.dateDialog()
    }
}