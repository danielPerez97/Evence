package daniel.perez.qrcameraview.di

import dagger.Subcomponent
import daniel.perez.qrcameraview.QrReaderActivity

@Subcomponent
interface QrReaderComponent
{
    @Subcomponent.Factory
    interface Factory
    {
        fun create(): QrReaderComponent
    }

    fun inject(activity: QrReaderActivity)
}