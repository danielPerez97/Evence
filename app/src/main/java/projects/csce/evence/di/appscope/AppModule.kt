package projects.csce.evence.di.appscope

import dagger.Module
import dagger.Provides
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
class AppModule
{
    @Provides @Singleton
    fun provideDialogStarter(): DialogStarter
    {
        return DialogStarterImpl()
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