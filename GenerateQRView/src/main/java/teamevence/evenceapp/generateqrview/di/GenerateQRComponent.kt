package teamevence.evenceapp.generateqrview.di

import dagger.Subcomponent
import teamevence.evenceapp.generateqrview.GenerateQR

@Subcomponent
interface GenerateQRComponent
{
    @Subcomponent.Factory
    interface Factory
    {
        fun create(): GenerateQRComponent
    }

    fun inject(activity: GenerateQR)
}