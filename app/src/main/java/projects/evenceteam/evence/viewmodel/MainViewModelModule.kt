package projects.evenceteam.evence.viewmodel

import teamevence.evenceapp.core.db.EventOps

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