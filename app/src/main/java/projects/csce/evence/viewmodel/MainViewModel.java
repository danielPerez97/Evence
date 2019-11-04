package projects.csce.evence.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import projects.csce.evence.service.model.Event;

public class MainViewModel extends ViewModel
{
    public LiveData<List<Event>> getCardData()
    {
        return new MutableLiveData<>();
    }
}