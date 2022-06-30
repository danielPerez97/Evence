package projects.evenceteam.evence.di.appscope

import coil.ImageLoader
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import teamevence.evenceapp.core.ActivityResultActions
import teamevence.evenceapp.core.ActivityStarter
import teamevence.evenceapp.core.DialogStarter
import teamevence.evenceapp.core.db.EventOps
import teamevence.evenceapp.core.service.FileManager
import projects.evenceteam.evence.ActivityResultActionsImpl
import projects.evenceteam.evence.ActivityStarterImpl
import projects.evenceteam.evence.DialogStarterImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule
{
    @Provides @Singleton
    fun provideDialogStarter(
            @Singleton imageLoader: ImageLoader,
            @Singleton activityStarter: ActivityStarter,
            @Singleton eventOps: EventOps ): DialogStarter
    {
        return DialogStarterImpl(imageLoader, activityStarter, eventOps)
    }

    @Provides @Singleton
    fun providerActivityStarter(fileManager: FileManager): ActivityStarter
    {
        return ActivityStarterImpl(fileManager)
    }

    @Provides @Singleton
    fun provideActivityResultActions(eventOps: EventOps, fileManager: FileManager): ActivityResultActions
    {
        return ActivityResultActionsImpl(eventOps, fileManager)
    }

    @Provides @Singleton
    fun provideMoshi(): Moshi
    {
        return Moshi.Builder().build()
    }
}