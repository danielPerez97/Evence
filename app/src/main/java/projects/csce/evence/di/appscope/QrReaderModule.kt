package projects.csce.evence.di.appscope

import android.content.Context
import dagger.Module
import dagger.Provides
import daniel.perez.qrcameraview.Camera.CameraHandler
import daniel.perez.qrcameraview.IntentActions
import daniel.perez.qrcameraview.Scanner.QRScanner
import daniel.perez.qrcameraview.Scanner.TextScanner
import javax.inject.Singleton

@Module
class QrReaderModule {

    @Provides
    @Singleton
    fun provideCameraHandler(context: Context, QRScanner: QRScanner, textScanner: TextScanner) : CameraHandler {
        return CameraHandler(context, QRScanner, textScanner)
    }

    @Provides
    @Singleton
    fun provideQrHandler() : QRScanner {
        return QRScanner()
    }

    @Provides
    @Singleton
    fun provideTextScanner() : TextScanner {
        return TextScanner()
    }

    @Provides
    fun provideIntentActions(context: Context) : IntentActions {
        return IntentActions(context)
    }
}