package daniel.perez.qrdialogview

import android.content.Context
import daniel.perez.core.model.DateSetEvent
import daniel.perez.core.model.TimeSetEvent
import daniel.perez.qrdialogview.rx.ObservableDatePickerDialog
import daniel.perez.qrdialogview.rx.ObservableTimePickerDialog
import daniel.perez.qrdialogview.rx.dateSetEvents
import daniel.perez.qrdialogview.rx.timeSetEvents
import io.reactivex.rxjava3.core.Observable
import java.util.*

class CalendarDialog(private val context: Context)
{
    fun timeDialog(): Observable<TimeSetEvent>
    {
        val calendar = Calendar.getInstance()

        val observableDialog: ObservableTimePickerDialog = timeSetEvents(context, calendar[Calendar.HOUR], calendar[Calendar.MINUTE])
        observableDialog.show()
        return observableDialog.timePickerEvents()
    }

    fun dateDialog(): Observable<DateSetEvent>
    {1
        val calendar = Calendar.getInstance()
        val observableDatePickerDialog: ObservableDatePickerDialog = dateSetEvents(context, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH])
        observableDatePickerDialog.show()

        return observableDatePickerDialog.datePickerEvents()
    }

    companion object
    {
        private const val TAG = "CalendarDialog"
    }

}