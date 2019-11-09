package projects.csce.evence.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import projects.csce.evence.service.model.Event;

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

    //for testing purposes. to populate the RecyclerView with a list of dummy data
    public List<Event> generateDummyData(){
       List<Event> dummyEvents = new ArrayList<>();
       dummyEvents.add(new Event());
       dummyEvents.add(new Event());
       dummyEvents.add(new Event());
       dummyEvents.add(new Event());
       dummyEvents.add(new Event());
       return dummyEvents;
    }

}