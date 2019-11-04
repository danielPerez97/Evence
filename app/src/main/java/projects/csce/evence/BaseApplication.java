package projects.csce.evence;

import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import projects.csce.evence.di.AppComponent;
import projects.csce.evence.di.DaggerAppComponent;
import projects.csce.evence.di.LoginModule;
import projects.csce.evence.di.NetworkModule;


public class BaseApplication extends Application
{

    private AppComponent injector;
    private GoogleSignInAccount account;

    @Override
    public void onCreate()
    {
        super.onCreate();
        injector = DaggerAppComponent.builder()
                .loginModule(new LoginModule(getApplicationContext()))
                .networkModule(new NetworkModule())
                .build();
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
}
