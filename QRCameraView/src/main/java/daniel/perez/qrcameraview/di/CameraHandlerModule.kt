package daniel.perez.qrcameraview.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import daniel.perez.qrcameraview.Camera.CameraHandler
import daniel.perez.qrcameraview.Scanner.QRScanner
import daniel.perez.qrcameraview.Scanner.TextScanner

@Module
@InstallIn(ActivityComponent::class)
class CameraHandlerModule
{
    @Provides
    fun provideCameraHandler(
            @ActivityContext context: Context,
            QRScanner: QRScanner,
            textScanner: TextScanner) : CameraHandler
    {
        return CameraHandler(context, QRScanner, textScanner)
    }
}