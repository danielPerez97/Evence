package daniel.perez.qrdialogview.rx

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker
import com.jakewharton.rxbinding4.internal.checkMainThread
import daniel.perez.core.model.DateSetEvent
import daniel.perez.core.model.Half
import daniel.perez.core.model.TimeSetEvent
import io.reactivex.rxjava3.android.MainThreadDisposable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

fun dateSetEvents(context: Context, year: Int, month: Int, dayOfMonth: Int): ObservableDatePickerDialog
{
    return DateSetEventObservable(context, year, month, dayOfMonth)
}

private class DateSetEventObservable(
        val context: Context,
        val year: Int,
        val month: Int,
        val dayOfMonth: Int
): Observable<DateSetEvent>(), ObservableDatePickerDialog
{
    var dialog: DatePickerDialog? = null

    override fun subscribeActual(observer: Observer<in DateSetEvent>)
    {
        if(!checkMainThread(observer)) {
            return
        }

        val listener = Listener(observer)
        dialog = DatePickerDialog(context, listener, year, month, dayOfMonth)
        observer.onSubscribe(listener)
        dialog?.show()
    }

    private inner class Listener( private val observer: Observer<in DateSetEvent> ): MainThreadDisposable(), DatePickerDialog.OnDateSetListener
    {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int)
        {
            if(!isDisposed)
            {
                val event = DateSetEvent(year = year, month = month + 1, dayOfMonth = dayOfMonth)
                observer.onNext(event)
            }
        }

        override fun onDispose()
        {
            dialog?.dismiss()
            dialog = null
        }
    }

    override fun datePickerEvents(): Observable<DateSetEvent>
    {
        return this
    }

    override fun show()
    {
        dialog?.show()
    }

}