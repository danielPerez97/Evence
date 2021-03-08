package projects.csce.evence.viewmodel

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import daniel.perez.core.db.EventOps
import javax.inject.Singleton

//@Module
//@InstallIn(ViewModelComponent::class)
class MainViewModelModule
{
//    @Provides
//    @ViewModelScoped
    fun provideEventOps(
            eventOps: EventOps
    ): EventOps
    {
        return eventOps
    }
}