package projects.csce.evence.di.appscope

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import projects.csce.evence.service.model.SharedPref
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {
    @Provides
    @Singleton
    fun provideSharedPref(
                @ApplicationContext context: Context
    ): SharedPref {
        return SharedPref(context)
    }
}