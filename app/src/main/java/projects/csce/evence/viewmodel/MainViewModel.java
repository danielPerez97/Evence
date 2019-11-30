package projects.csce.evence.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import projects.csce.evence.service.model.event.Event;

public class MainViewModel extends ViewModel
{

    @Inject
    MainViewModel(List<Event> dummyEvents)
    {
        this.dummyEvents = dummyEvents;
    }

    private LiveData<List<Event>> eventsList;

    public LiveData<List<Event>> getCardData()
    {
        return new MutableLiveData<>();
    }

    public LiveData<List<Event>> getEventsList()
    {
        return eventsList;
    }

//    public LiveData<List<Event>> getEventsFromFileSystem()
//    {
//        // Get the events written to the file system in the directory
//    }

    List<Event> dummyEvents;

    //for testing purposes. to populate the RecyclerView with a list of dummy data
    public List<Event> generateDummyData()
    {
       return dummyEvents;
    }

}