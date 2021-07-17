package daniel.perez.qrdialogview.rx

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import com.jakewharton.rxbinding4.internal.checkMainThread
import daniel.perez.core.model.Half
import daniel.perez.core.model.TimeSetEvent
import io.reactivex.rxjava3.android.MainThreadDisposable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

fun timeSetEvents(context: Context, hour: Int, minute: Int): ObservableTimePickerDialog
{
    return TimeSetEventObservable(context, hour, minute)
}

private class TimeSetEventObservable(
        val context: Context,
        val hour: Int,
        val minute: Int
): Observable<TimeSetEvent>(), ObservableTimePickerDialog
{
     var dialog: TimePickerDialog? = null

    override fun subscribeActual(observer: Observer<in TimeSetEvent>)
    {
        if(!checkMainThread(observer)) {
            return
        }

        val listener = Listener(observer)
        dialog = TimePickerDialog(context, listener, hour, minute, false)
        observer.onSubscribe(listener)
        dialog?.show()
    }

    private inner class Listener( private val observer: Observer<in TimeSetEvent>): MainThreadDisposable(), TimePickerDialog.OnTimeSetListener
    {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int)
        {
            if(!isDisposed)
            {
                val event = TimeSetEvent(hourOfDay, minute, half = if(hourOfDay < 12) Half.AM else Half.PM)
                observer.onNext(event)
            }

        }

        override fun onDispose()
        {
            dialog?.dismiss()
            dialog = null
        }
    }

    override fun timePickerEvents(): Observable<TimeSetEvent>
    {
        return this
    }

    override fun show()
    {
        dialog?.show()
    }

}