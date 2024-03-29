package teamevence.evenceapp.qrdialogview

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import teamevence.evenceapp.core.model.DateSetEvent
import teamevence.evenceapp.core.model.Half
import teamevence.evenceapp.core.model.TimeSetEvent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

class CalendarDialog(private val context: Context)
{
    private val timeEvents = PublishSubject.create<TimeSetEvent>()
    private val dateEvents = PublishSubject.create<DateSetEvent>()

    fun timeDialog(): Observable<TimeSetEvent>
    {
        val calendar = Calendar.getInstance()
        val listener = TimePickerDialog.OnTimeSetListener { _, newHour, newMinute ->
            timeEvents.onNext( TimeSetEvent(newHour, newMinute, if(newHour < 12) Half.AM else Half.PM) )
        }
        val timePickerDialog = TimePickerDialog(context, listener, calendar[Calendar.HOUR], calendar[Calendar.MINUTE], false)
        timePickerDialog.show()
        return timeEvents
    }

    fun dateDialog(): Observable<DateSetEvent>
    {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(context, { _, newYear, newMonth, dayOfMonth ->
            dateEvents.onNext( DateSetEvent(newMonth + 1, dayOfMonth, newYear) )
        }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH])
        datePickerDialog.show()

        return dateEvents
    }

    companion object
    {
        private const val TAG = "CalendarDialog"
    }

}