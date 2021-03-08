package projects.csce.evence.di.appscope

import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import daniel.perez.core.ActivityResultActions
import daniel.perez.core.DialogStarter
import daniel.perez.core.ActivityStarter
import daniel.perez.core.db.EventOps
import daniel.perez.core.service.FileManager
import projects.csce.evence.ActivityResultActionsImpl
import projects.csce.evence.DialogStarterImpl
import projects.csce.evence.ActivityStarterImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule
{
    @Provides @Singleton
    fun provideDialogStarter(@Singleton imageLoader: ImageLoader, @Singleton activityStarter: ActivityStarter ): DialogStarter
    {
        return DialogStarterImpl(imageLoader, activityStarter)
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
}