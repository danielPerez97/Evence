package daniel.perez.qrcameraview.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ActivityContext
import daniel.perez.qrcameraview.IntentActions
import daniel.perez.qrcameraview.Scanner.QRScanner
import daniel.perez.qrcameraview.Scanner.TextScanner

@Module
@InstallIn(ActivityRetainedComponent::class)
class QrReaderModule
{
    @Provides
    fun provideQrHandler() : QRScanner
    {
        return QRScanner()
    }

    @Provides
    fun provideTextScanner() : TextScanner
    {
        return TextScanner()
    }

    @Provides
    fun provideIntentActions(
            @ActivityContext context: Context
    ) : IntentActions
    {
        return IntentActions(context)
    }
}