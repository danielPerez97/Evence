package projects.csce.evence.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import projects.csce.evence.ical.EventSpec;
import projects.csce.evence.service.model.FileManager;

public class MainViewModel extends ViewModel
{

    private FileManager fileManager;

    @Inject
    MainViewModel(FileManager fileManager)
    {
        this.fileManager = fileManager;
    }

    public LiveData<List<EventSpec>> liveFiles()
    {
        return LiveDataReactiveStreams.fromPublisher(fileManager.files());
    }

}