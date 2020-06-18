package projects.csce.evence;

import android.app.Application;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import daniel.perez.core.BaseActivity;
import daniel.perez.core.DialogStarter;
import daniel.perez.core.model.ViewCalendarData;
import daniel.perez.core.service.FileManager;
import daniel.perez.generateqrview.di.GenerateQRComponent;
import daniel.perez.generateqrview.di.GenerateQRComponentProvider;
import daniel.perez.qrcameraview.di.QrReaderComponent;
import daniel.perez.qrcameraview.di.QrReaderComponentProvider;
import daniel.perez.qrdialogview.QRDialog;
import projects.csce.evence.di.appscope.AppComponent;
import projects.csce.evence.di.appscope.DaggerAppComponent;
import projects.csce.evence.di.appscope.LoginModule;
import projects.csce.evence.di.appscope.QrModule;
import projects.csce.evence.service.model.SharedPref;
import projects.csce.evence.view.ui.CalendarDialog;
import timber.log.Timber;


public class BaseApplication extends Application implements QrReaderComponentProvider, DialogStarter, GenerateQRComponentProvider
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
                .qrModule( new QrModule(this) )
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

    @Override
    public void startQrDialog(@NotNull BaseActivity activity, @NotNull ViewCalendarData data)
    {
        new QRDialog(activity, data, fileManager);
    }

    @Override
    public void startTimeDialog(@NotNull BaseActivity activity, @NotNull TextView textView)
    {
        CalendarDialog calendarDialog = new CalendarDialog(activity, textView);
        calendarDialog.timeDialog();
    }

    @Override
    public void startDateDialog(@NotNull BaseActivity activity, @NotNull TextView textView)
    {
        CalendarDialog calendarDialog = new CalendarDialog(activity, textView);
        calendarDialog.dateDialog();
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
}
