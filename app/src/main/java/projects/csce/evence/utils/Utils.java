package projects.csce.evence.utils;

import androidx.appcompat.app.AppCompatActivity;

import projects.csce.evence.base.BaseApplication;
import projects.csce.evence.di.AppComponent;

public class Utils
{
    // This class should never be instantiated
    private Utils() { }

    // Simplify getting the Injector across activities
    public static AppComponent getAppComponent(AppCompatActivity activity)
    {
        return ((BaseApplication) activity.getApplication()).getInjector();
    }

//    static LoggedInSubComponent getLoggedInSubcomponent(AppCompatActivity activity)
//    {
//
//    }
}
