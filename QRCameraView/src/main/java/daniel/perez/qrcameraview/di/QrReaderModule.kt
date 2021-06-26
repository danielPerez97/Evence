package daniel.perez.qrcameraview.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import daniel.perez.qrcameraview.Camera.CameraHandler
import daniel.perez.qrcameraview.IntentActions
import daniel.perez.qrcameraview.Scanner.QRScanner
import daniel.perez.qrcameraview.Scanner.TextScanner
import daniel.perez.qrcameraview.ui.BarcodeTypes

@Module
@InstallIn(ActivityRetainedComponent::class)
class QrReaderModule
{
    @Provides
    @ActivityRetainedScoped
    fun provideQrHandler() : QRScanner
    {
        return QRScanner()
    }

    @Provides
    @ActivityRetainedScoped
    fun provideTextScanner() : TextScanner
    {
        return TextScanner()
    }

    @Provides
    @ActivityRetainedScoped
    fun provideCameraHandler(
        @ApplicationContext context: Context,
        QRScanner: QRScanner,
        textScanner: TextScanner) : CameraHandler
    {
        return CameraHandler(context, QRScanner, textScanner)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideBarcodeTypes(
        @ApplicationContext context: Context
    ): BarcodeTypes
    {
        return BarcodeTypes(context)
    }


    @Provides
    @ActivityRetainedScoped
    fun provideIntentActions(
            @ApplicationContext context: Context
    ) : IntentActions
    {
        return IntentActions(context)
    }
}