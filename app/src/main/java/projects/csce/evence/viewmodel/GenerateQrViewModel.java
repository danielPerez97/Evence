package projects.csce.evence.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;
import projects.csce.evence.service.model.qr.QrAttempt;
import projects.csce.evence.service.model.qr.Qr;
import projects.csce.evence.service.model.qr.QrBitmapGenerator;

public class GenerateQrViewModel extends ViewModel
{
    private MutableLiveData<QrAttempt> images = new MutableLiveData<>();
    private QrBitmapGenerator generator = new QrBitmapGenerator();
    private PublishProcessor<Qr> attempts = PublishProcessor.create();


    public void generateQrBitmap(Qr qr)
    {
        attempts.onNext(qr);
    }

    public LiveData<QrAttempt> qrImages()
    {
        Flowable<QrAttempt> publisher = attempts
                .flatMap( it ->
                        Flowable.just(it)
                                .map(qr -> ((QrAttempt) new QrAttempt.Success(generator.generate(qr))))
                                .onErrorReturn(QrAttempt.Failure::new));

        return LiveDataReactiveStreams.fromPublisher(publisher);
    }

}
