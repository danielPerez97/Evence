package teamevence.evenceapp.fileselectview.di

import dagger.Subcomponent
import teamevence.evenceapp.fileselectview.FileSelectActivity

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