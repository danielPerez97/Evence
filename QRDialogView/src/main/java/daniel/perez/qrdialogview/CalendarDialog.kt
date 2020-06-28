package daniel.perez.qrdialogview

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import daniel.perez.core.model.DateSetEvent
import daniel.perez.core.model.Half
import daniel.perez.core.model.TimeSetEvent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*

class CalendarDialog(private val context: Context)
{
    private val timeEvents = PublishSubject.create<TimeSetEvent>()
    private val dateEvents = PublishSubject.create<DateSetEvent>()

    fun timeDialog(): Observable<TimeSetEvent>
    {
        val calendar = Calendar.getInstance()
        val listener = TimePickerDialog.OnTimeSetListener { _, newHour, newMinute ->
            timeEvents.onNext(TimeSetEvent(newHour, newMinute, if(newHour < 12) Half.AM else Half.PM))
        }
        val timePickerDialog = TimePickerDialog(context, listener, calendar[Calendar.HOUR], calendar[Calendar.MINUTE], false)
        timePickerDialog.show()
        return timeEvents
    }

    fun dateDialog(): Observable<DateSetEvent>
    {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, newYear, newMonth, dayOfMonth ->
            dateEvents.onNext( DateSetEvent(newMonth, dayOfMonth, newYear) )
        }, calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1, calendar[Calendar.DAY_OF_MONTH])
        datePickerDialog.show()

        return dateEvents
    }

    companion object
    {
        private const val TAG = "CalendarDialog"
    }

}