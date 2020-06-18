package projects.csce.evence.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import daniel.perez.ical.EventSpec;
import daniel.perez.ical.ICalSpec;
import io.reactivex.Observable;
import daniel.perez.core.service.FileManager;
import daniel.perez.core.service.qr.QrBitmapGenerator;
import daniel.perez.core.model.ViewCalendarData;
import daniel.perez.core.model.ViewEvent;

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