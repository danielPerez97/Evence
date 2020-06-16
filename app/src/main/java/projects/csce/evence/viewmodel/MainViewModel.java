package projects.csce.evence.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.inject.Inject;

import daniel.perez.ical.EventSpec;
import daniel.perez.ical.ICalSpec;
import io.reactivex.Observable;
import projects.csce.evence.service.model.FileManager;
import projects.csce.evence.service.model.qr.QrBitmapGenerator;
import projects.csce.evence.view.ui.model.ViewCalendarData;
import projects.csce.evence.view.ui.model.ViewEvent;

public class MainViewModel extends ViewModel
{

    private FileManager fileManager;
    private QrBitmapGenerator generator;

    @Inject
    MainViewModel(FileManager fileManager, QrBitmapGenerator generator)
    {
        this.fileManager = fileManager;
        this.generator = generator;
    }

    public Observable<List<ViewCalendarData>> liveFiles()
    {

        return fileManager.icals()
                .flatMap(iCalSpecs ->
                        Observable.just(iCalSpecs)
                                .flatMapIterable(iCalSpec -> iCalSpecs)
                                .map( (ICalSpec iCalSpec) -> {
                                    List<ViewEvent> events = map(iCalSpec.getEvents());
                                    return new ViewCalendarData(iCalSpec.getFileName(), events);
                                })
                                .toList()
                                .toObservable()
                );
    }

    private List<ViewEvent> map(List<EventSpec> eventSpecs)
    {
        return eventSpecs.stream()
                .map(event -> new ViewEvent(
                        event.getTitle(),
                        event.getLocation(),
                        event.getStartDate(),
                        event.getStartTime(),
                        event.getStartInstantEpoch(),
                        event.getEndEpochMilli(),
                        event.getLocation(),
                        event.text(),
                        generator.forceGenerate(event.text())

                ))
                .collect(Collectors.toList());
    }

}