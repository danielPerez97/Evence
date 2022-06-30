package teamevence.evenceapp.qrcameraview.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import teamevence.evenceapp.qrcameraview.Camera.CameraHandler
import teamevence.evenceapp.qrcameraview.Scanner.QRScanner
import teamevence.evenceapp.qrcameraview.Scanner.TextScanner

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

}