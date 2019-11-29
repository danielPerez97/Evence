package projects.csce.evence.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import org.threeten.bp.Month;
import org.threeten.bp.ZonedDateTime;

import java.io.File;

import javax.inject.Inject;

import projects.csce.evence.ical.EventSpec;
import projects.csce.evence.ical.ICalSpec;
import projects.csce.evence.service.model.event.Event;
import projects.csce.evence.service.model.qr.QrAttempt;
import projects.csce.evence.service.model.qr.QrBitmapGenerator;

public class GenerateQrViewModel extends ViewModel
{
    private QrBitmapGenerator generator;
    private Context context;

    @Inject
    GenerateQrViewModel(QrBitmapGenerator generator, Context context)
    {
        this.generator = generator;
        this.context = context;
    }


    public void generateQrBitmap(Event event)
    {
        generator.generate(event);
    }

    public LiveData<QrAttempt> qrImages()
    {

        return LiveDataReactiveStreams.fromPublisher(generator.generations());
    }

    public void saveBitmap(Event event, Bitmap bitmap)
    {
        event.newBuilder().title("fdjskfldjask");
    }

    public void saveEvent(Event event)
    {


    }

    public void iCalExample()
    {
        // Create the ZonedDateTime object for a birthday
        ZonedDateTime birthDate = ZonedDateTime.now()
                .withMonth(Month.NOVEMBER.getValue())
                .withDayOfMonth(24)
                .withYear(2019)
                .withHour(12)
                .withMinute(30);

        // Create the ZonedDateTime object for thanksgiving
        ZonedDateTime thanksgiving = ZonedDateTime.now()
                .withMonth(Month.NOVEMBER.getValue())
                .withDayOfMonth(28)
                .withYear(2019)
                .withHour(12)
                .withMinute(0);

        // Create the EventSpec for the birthday
        EventSpec birthday = EventSpec.builder(0)
                .title("Daniel's Birthday")
                .start(birthDate)
                .end(birthDate.plusHours(1))
                .build();

        // Create the EventSpec for thanksgiving
        EventSpec thanksSpec = EventSpec.builder(1)
                .title("Thanksgiving")
                .start(thanksgiving)
                .end(thanksgiving.plusHours(1))
                .build();

        // Create the ICalSpec
        ICalSpec ical = ICalSpec.builder()
                .addEvent(birthday)
                .addEvent(thanksSpec)
                .build();

        // Create the ICal File
        File birthdayFile = ical.file();
    }
}
