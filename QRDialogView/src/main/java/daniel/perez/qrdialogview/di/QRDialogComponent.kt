package daniel.perez.qrdialogview.di

import dagger.Subcomponent
import daniel.perez.qrdialogview.QRDialog

@Subcomponent
interface QRDialogComponent
{
    @Subcomponent.Factory
    interface Factory
    {
        fun create(): QRDialogComponent
    }

    fun inject(dialog: QRDialog)
}