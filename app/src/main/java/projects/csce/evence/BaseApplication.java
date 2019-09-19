package projects.csce.evence;

import android.app.Application;

import projects.csce.evence.di.DaggerNetworkInjector;
import projects.csce.evence.di.NetworkInjector;


public class BaseApplication extends Application
{

    NetworkInjector injector;
    @Override
    public void onCreate()
    {
        super.onCreate();
        injector = DaggerNetworkInjector.create();
    }

    public NetworkInjector getInjector()
    {
        return injector;
    }
}
