package projects.csce.evence.di.appscope

import coil.ImageLoader
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import daniel.perez.core.ActivityResultActions
import daniel.perez.core.ActivityStarter
import daniel.perez.core.DialogStarter
import daniel.perez.core.db.EventOps
import daniel.perez.core.service.FileManager
import projects.csce.evence.ActivityResultActionsImpl
import projects.csce.evence.ActivityStarterImpl
import projects.csce.evence.DialogStarterImpl
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