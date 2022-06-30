package projects.evenceteam.evence.di.appscope

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import teamevence.evenceapp.licensesview.LicenseRetriever
import projects.evenceteam.evence.utils.LicenseRetrieverImpl

@Module
@InstallIn(ActivityComponent::class)
class LicensesModule()
{
    @Provides
    @ActivityScoped
    fun provideLicenseRetriever(
            moshi: Moshi,
            @ActivityContext context: Context
    ): LicenseRetriever
    {
        return LicenseRetrieverImpl(moshi, context)
    }
}