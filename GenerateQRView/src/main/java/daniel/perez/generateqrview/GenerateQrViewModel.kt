package daniel.perez.generateqrview;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import daniel.perez.ical.EventSpec;
import daniel.perez.ical.ICalSpec;
import daniel.perez.core.service.FileManager;
import daniel.perez.core.service.qr.QrAttempt;
import daniel.perez.core.service.qr.QrBitmapGenerator;
import io.reactivex.rxjava3.core.Flowable;

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

    public Flowable<QrAttempt> qrImages()
    {

        return generator.generations();
    }

    public void saveFile(ICalSpec ical)
    {
        fileManager.saveICalFile(ical);
    }
}
