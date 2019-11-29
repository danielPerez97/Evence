package projects.csce.evence;

import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.jakewharton.threetenabp.AndroidThreeTen;

import projects.csce.evence.di.appscope.AppComponent;
import projects.csce.evence.di.appscope.DaggerAppComponent;
import projects.csce.evence.di.appscope.LoginModule;


public class BaseApplication extends Application
{

    private AppComponent injector;
    private GoogleSignInAccount account;

    @Override
    public void onCreate()
    {
        super.onCreate();
        AndroidThreeTen.init(this);
        injector = DaggerAppComponent.builder()
                .loginModule(new LoginModule(getApplicationContext()))
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
