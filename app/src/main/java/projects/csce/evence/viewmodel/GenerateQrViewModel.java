package projects.csce.evence.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import daniel.perez.ical.EventSpec;
import daniel.perez.ical.ICalSpec;
import projects.csce.evence.service.model.FileManager;
import projects.csce.evence.service.model.qr.QrAttempt;
import projects.csce.evence.service.model.qr.QrBitmapGenerator;

public class GenerateQrViewModel extends ViewModel
{
    private QrBitmapGenerator generator;
    private Context context;
    private FileManager fileManager;

    @Inject
    GenerateQrViewModel(QrBitmapGenerator generator, Context context, FileManager fileManager)
    {
        this.generator = generator;
        this.context = context;
        this.fileManager = fileManager;
    }


    public void generateQrBitmap(EventSpec event)
    {
        generator.generate(event);
    }

    public LiveData<QrAttempt> qrImages()
    {

        return LiveDataReactiveStreams.fromPublisher(generator.generations());
    }

    public void saveFile(ICalSpec ical)
    {
        fileManager.saveICalFile(ical);
    }
}
