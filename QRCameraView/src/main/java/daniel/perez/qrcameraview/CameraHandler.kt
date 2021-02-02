package daniel.perez.qrcameraview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.common.InputImage
import timber.log.Timber
import java.util.concurrent.Executors

class CameraHandler(private val context: Context, private val qrHandler : QrHandler) {
    private lateinit var camera: Camera
    lateinit var imageProxy: ImageProxy

    fun setupCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val cameraExecutor = Executors.newSingleThreadExecutor()

        cameraProviderFuture.addListener(Runnable {
           //val displayMetrics = DisplayMetrics()
            val displayMetrics = Resources.getSystem().displayMetrics

            val rect = Rect()

            //provides CPU-accessible buffers for analysis, such as for machine learning.
            val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(displayMetrics.widthPixels, displayMetrics.heightPixels))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
            imageAnalysis.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer {
                imageProxy = it
                analyzeImage(SCAN_TYPE.BARCODE)
            })

            //sets up surface for displaying the image preview
            val preview = Preview.Builder()
                    .setTargetResolution(Size(displayMetrics.widthPixels, displayMetrics.heightPixels))
                    .build()

            //chooses a camera
            val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
            val cameraProvider = cameraProviderFuture.get()
                    ?: throw IllegalStateException("Camera initialization failed.")
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, imageAnalysis, preview)
            preview.setSurfaceProvider(previewView.createSurfaceProvider(camera.cameraInfo))

            previewView.scaleType = PreviewView.ScaleType.FIT_START

            Timber.i("************PreviewView=" + previewView.width + " "+ previewView.height)
            Timber.i("************DeviceView=" + displayMetrics.widthPixels + " "+ displayMetrics.heightPixels)

        }, ContextCompat.getMainExecutor(context))
    }

    //analyzes the image and reads the barcode
    fun analyzeImage(scanType : SCAN_TYPE) {
        when (scanType) {
            SCAN_TYPE.BARCODE -> scanBarcode()
            SCAN_TYPE.TEXT -> scanText()
        }
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun scanBarcode() {
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
            qrHandler.scanBarcode(imageProxy, image)
        }
    }

    fun scanText() {
        //future feature
    }

    fun toggleFlash(flashOn: Boolean) {
        camera.cameraControl.enableTorch(!flashOn)
    }

    enum class SCAN_TYPE {
        BARCODE,
        TEXT, //possible future use
        FACE //possible future use
    }
}
