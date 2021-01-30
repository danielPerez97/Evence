package daniel.perez.qrcameraview

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

class CameraSetup(
        private val context: Context,
        private val lifecycleOwner: LifecycleOwner,
        private val previewView: PreviewView
        //,private val boundingGraphic:
) {
    private lateinit var camera: Camera

    init {
        setupCamera()
    }

    private fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val cameraExecutor = Executors.newSingleThreadExecutor()

        cameraProviderFuture.addListener(Runnable {
            val displayMetrics = DisplayMetrics()

            //provides CPU-accessible buffers for analysis, such as for machine learning.
            val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(displayMetrics.widthPixels, displayMetrics.heightPixels))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
            imageAnalysis.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { imageProxy -> analyzeImage(imageProxy) })

            //sets up surface for displaying the image preview
            val preview = Preview.Builder()
                    .build()
            preview.setSurfaceProvider(previewView.createSurfaceProvider(null))

            //chooses a camera
            val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
            val cameraProvider = cameraProviderFuture.get()
                    ?: throw IllegalStateException("Camera initialization failed.")
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, imageAnalysis, preview)

        }, ContextCompat.getMainExecutor(context))
    }

    //analyzes the image and reads the barcode
    @SuppressLint("UnsafeExperimentalUsageError")
    private fun analyzeImage(imageProxy: ImageProxy) {
        val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_AZTEC,
                        Barcode.FORMAT_UPC_A,
                        Barcode.FORMAT_UPC_E
                )
                .build()
        val mediaImage = imageProxy.image

        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            QrHandler(context, imageProxy, image)
        }
    }
}
