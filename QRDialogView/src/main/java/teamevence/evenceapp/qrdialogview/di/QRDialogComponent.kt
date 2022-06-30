package teamevence.evenceapp.qrdialogview.di

import dagger.Subcomponent
import teamevence.evenceapp.qrdialogview.QRDialog

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