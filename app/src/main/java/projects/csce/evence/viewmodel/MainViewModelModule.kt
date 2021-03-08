package projects.csce.evence.viewmodel

import daniel.perez.core.db.EventOps

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