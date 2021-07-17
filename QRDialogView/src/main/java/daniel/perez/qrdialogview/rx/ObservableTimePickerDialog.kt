package daniel.perez.qrdialogview.rx

import daniel.perez.core.model.TimeSetEvent
import io.reactivex.rxjava3.core.Observable

interface ObservableTimePickerDialog
{
    fun timePickerEvents(): Observable<TimeSetEvent>

    fun show()
}