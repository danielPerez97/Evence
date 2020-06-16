package projects.csce.evence;

import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import projects.csce.evence.di.appscope.AppComponent;
import projects.csce.evence.di.appscope.DaggerAppComponent;
import projects.csce.evence.di.appscope.LoginModule;
import projects.csce.evence.service.model.SharedPref;


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
                .build();

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
}
