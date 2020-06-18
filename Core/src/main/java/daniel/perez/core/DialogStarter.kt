package daniel.perez.core

import android.widget.TextView
import daniel.perez.core.model.ViewCalendarData

interface DialogStarter
{
    fun startQrDialog(activity: BaseActivity, data: ViewCalendarData)
    fun startTimeDialog(activity: BaseActivity, textView: TextView)
    fun startDateDialog(activity: BaseActivity, textView: TextView)
}