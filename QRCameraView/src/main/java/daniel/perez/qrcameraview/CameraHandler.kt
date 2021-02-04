package daniel.perez.qrcameraview


import android.content.Context
import android.content.res.Resources
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class CameraHandler @Inject constructor(private val context: Context,
                                        private val qrScanner: QRScanner,
                                        private val textScanner: TextScanner) {

    private lateinit var camera: Camera
    lateinit var imageProxy: ImageProxy
    private lateinit var cameraProvider : ProcessCameraProvider
    private lateinit var cameraExecutor : ExecutorService
    private var currentScanMode : SCAN_TYPE = SCAN_TYPE.BARCODE
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var previewView: PreviewView

    init {
        setup()
    }

    private fun setup() {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    fun openCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        this.lifecycleOwner = lifecycleOwner
        this.previewView = previewView
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(Runnable {
            val displayMetrics = Resources.getSystem().displayMetrics

            //provides CPU-accessible buffers for analysis, such as for machine learning.
            val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(displayMetrics.widthPixels, displayMetrics.heightPixels))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
            imageAnalysis.setAnalyzer(cameraExecutor, selectScanType())

            //sets up surface for displaying the image preview
            val preview = Preview.Builder()
                    .setTargetResolution(Size(displayMetrics.widthPixels, displayMetrics.heightPixels))
                    .build()

            //chooses a camera
            val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
            cameraProvider = cameraProviderFuture.get()
                    ?: throw IllegalStateException("Camera initialization failed.")
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, imageAnalysis, preview)
            preview.setSurfaceProvider(previewView.createSurfaceProvider(camera.cameraInfo))
            previewView.scaleType = PreviewView.ScaleType.FIT_START

        }, ContextCompat.getMainExecutor(context))
    }

    fun selectScanType() : ImageAnalysis.Analyzer{
        when (currentScanMode) {
            SCAN_TYPE.BARCODE -> return qrScanner
            SCAN_TYPE.TEXT -> return textScanner
            else -> return qrScanner
        }
    }

    fun switchScanType(currentMode: SCAN_TYPE) : SCAN_TYPE{
        cameraProvider.unbindAll()
        lateinit var newCurrentMode : SCAN_TYPE
        //switch to other scan type
        when (currentMode) {
            SCAN_TYPE.BARCODE -> {
                openCamera(lifecycleOwner,previewView)
                newCurrentMode = SCAN_TYPE.TEXT
            }
            SCAN_TYPE.TEXT -> {
                openCamera(lifecycleOwner,previewView)
                newCurrentMode = SCAN_TYPE.BARCODE
            }
            else -> newCurrentMode = SCAN_TYPE.BARCODE
        }
        return newCurrentMode
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
