package daniel.perez.generateqrview.di

import dagger.Subcomponent
import daniel.perez.generateqrview.GenerateQR

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