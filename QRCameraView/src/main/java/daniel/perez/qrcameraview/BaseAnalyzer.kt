package daniel.perez.qrcameraview

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage

abstract class BaseAnalyzer : ImageAnalysis.Analyzer {
    protected lateinit var  inputImage : InputImage
    protected lateinit var imageProxy: ImageProxy

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        this.imageProxy = imageProxy
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        }
        scan()
    }

    abstract fun scan()
}