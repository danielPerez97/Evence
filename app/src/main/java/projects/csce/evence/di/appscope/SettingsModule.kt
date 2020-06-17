package projects.csce.evence.di.appscope

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import projects.csce.evence.service.model.SharedPref
import javax.inject.Singleton

@Module
class SettingsModule {
    @Provides
    @Singleton
    fun provideSharedPref(context: Context): SharedPref {
        return SharedPref(context)
    }
}