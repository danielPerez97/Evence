package daniel.perez.fileselectview.di

import dagger.Subcomponent
import daniel.perez.fileselectview.FileSelectActivity

@Subcomponent
interface FileSelectComponent
{
    @Subcomponent.Factory
    interface Factory
    {
        fun create(): FileSelectComponent
    }

    fun inject(activity: FileSelectActivity)
}