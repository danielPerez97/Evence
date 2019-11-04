package projects.csce.evence.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import projects.csce.evence.BaseApplication;
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

    public static void toastShort(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
