package projects.evenceteam.evence

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import projects.evenceteam.evence.service.model.SharedPref
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp
class BaseApplication : Application()
{
    override fun onCreate()
    {
        super.onCreate()

        // Setup Timber
        if (BuildConfig.DEBUG)
        {
            Timber.plant(DebugTree())
        }
        SharedPref(applicationContext).setSavedPreferences()
    }
}