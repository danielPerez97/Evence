package daniel.perez.qrdialogview.rx

import daniel.perez.core.model.DateSetEvent
import io.reactivex.rxjava3.core.Observable

interface ObservableDatePickerDialog
{
    fun datePickerEvents(): Observable<DateSetEvent>

    fun show()
}