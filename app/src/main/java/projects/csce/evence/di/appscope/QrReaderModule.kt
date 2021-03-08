package projects.csce.evence.di.appscope

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import daniel.perez.qrcameraview.Camera.CameraHandler
import daniel.perez.qrcameraview.IntentActions
import daniel.perez.qrcameraview.Scanner.QRScanner
import daniel.perez.qrcameraview.Scanner.TextScanner
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class QrReaderModule {

    @Provides
    @Singleton
    fun provideCameraHandler(
            @ApplicationContext context: Context,
            QRScanner: QRScanner,
            textScanner: TextScanner) : CameraHandler
    {
        return CameraHandler(context, QRScanner, textScanner)
    }

    @Provides
    @Singleton
    fun provideQrHandler() : QRScanner
    {
        return QRScanner()
    }

    @Provides
    @Singleton
    fun provideTextScanner() : TextScanner
    {
        return TextScanner()
    }

    @Provides
    fun provideIntentActions(
            @ApplicationContext context: Context
    ) : IntentActions
    {
        return IntentActions(context)
    }
}