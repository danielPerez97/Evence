package projects.csce.evence;

import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import daniel.perez.core.service.FileManager;
import daniel.perez.generateqrview.di.GenerateQRComponent;
import daniel.perez.generateqrview.di.GenerateQRComponentProvider;
import daniel.perez.qrcameraview.di.QrReaderComponent;
import daniel.perez.qrcameraview.di.QrReaderComponentProvider;
import daniel.perez.qrdialogview.di.QRDialogComponent;
import daniel.perez.qrdialogview.di.QRDialogComponentProvider;
import projects.csce.evence.di.appscope.AppComponent;
import projects.csce.evence.di.appscope.DaggerAppComponent;
import projects.csce.evence.di.appscope.LoginModule;
import projects.csce.evence.service.model.SharedPref;
import timber.log.Timber;


public class BaseApplication extends Application implements QrReaderComponentProvider, GenerateQRComponentProvider, QRDialogComponentProvider
{

    private AppComponent injector;
    private GoogleSignInAccount account;
    @Inject FileManager fileManager;

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Setup Timber
        if(BuildConfig.DEBUG)
        {
            Timber.plant(new Timber.DebugTree());
        }

        injector = DaggerAppComponent.builder()
                .loginModule( new LoginModule( getApplicationContext() ) )
                .build();

        injector.inject(this);

        new SharedPref(getApplicationContext()).setSavedPreferences();

    }



    public AppComponent getInjector()
    {
        return injector;
    }

    public void setAccount(GoogleSignInAccount account)
    {
        this.account = account;
    }

    public GoogleSignInAccount getAccount()
    {
        return account;
    }

    @NotNull
    @Override
    public QrReaderComponent getQrReaderComponent()
    {
        return injector.provideQrReaderFactory().create();
    }

    @NotNull
    @Override
    public GenerateQRComponent provideGenerateQRComponent()
    {
        return injector.provideGenerateQrFactory().create();
    }

    @NotNull
    @Override
    public QRDialogComponent provideQrDialogComponent()
    {
        return injector.providerQrDialogFactory().create();
    }
}
