package projects.csce.evence.di.appscope

import android.content.Context
import dagger.Module
import dagger.Provides
import daniel.perez.qrcameraview.CameraHandler
import daniel.perez.qrcameraview.IntentActions
import daniel.perez.qrcameraview.QrHandler
import javax.inject.Singleton

@Module
class QrReaderModule {

    @Provides
    @Singleton
    fun provideCameraHandler(context: Context,  qrHandler: QrHandler ) :  CameraHandler {
        return CameraHandler(context, qrHandler)
    }

    @Provides
    @Singleton
    fun provideQrHandler() : QrHandler {
        return QrHandler()
    }

    @Provides
    fun provideIntentActions(context: Context) : IntentActions {
        return IntentActions(context)
    }
}