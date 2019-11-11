package projects.csce.evence.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import projects.csce.evence.service.model.event.Event;

public class MainViewModel extends ViewModel
{
    private LiveData<List<Event>> eventsList;

    public LiveData<List<Event>> getCardData()
    {
        return new MutableLiveData<>();
    }

    public LiveData<List<Event>> getEventsList()
    {
        return eventsList;
    }

    List<Event> dummyEvents;

    @Inject MainViewModel(List<Event> dummyEvents)
    {
        this.dummyEvents = dummyEvents;
    }

    //for testing purposes. to populate the RecyclerView with a list of dummy data
    public List<Event> generateDummyData()
    {
       return dummyEvents;
    }

}