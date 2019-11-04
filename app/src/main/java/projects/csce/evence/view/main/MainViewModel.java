package projects.csce.evence.view.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import projects.csce.evence.utils.Event;

public class MainViewModel extends ViewModel
{
    public LiveData<List<Event>> getCardData()
    {
        return new MutableLiveData<>();
    }
}
