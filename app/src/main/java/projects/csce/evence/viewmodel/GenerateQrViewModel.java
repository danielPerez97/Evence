package projects.csce.evence.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import projects.csce.evence.service.model.event.Event;
import projects.csce.evence.service.model.qr.QrAttempt;
import projects.csce.evence.service.model.qr.QrBitmapGenerator;

public class GenerateQrViewModel extends ViewModel
{
    private QrBitmapGenerator generator;

    @Inject
    GenerateQrViewModel(QrBitmapGenerator generator)
    {
        this.generator = generator;
    }


    public void generateQrBitmap(Event qr)
    {
        generator.generate(qr);
    }

    public LiveData<QrAttempt> qrImages()
    {

        return LiveDataReactiveStreams.fromPublisher(generator.generations());
    }

}
