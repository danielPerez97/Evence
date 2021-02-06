package daniel.perez.qrcameraview.Camera


import android.content.Context
import android.content.res.Resources
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import daniel.perez.qrcameraview.Scanner.BaseAnalyzer
import daniel.perez.qrcameraview.Scanner.QRScanner
import daniel.perez.qrcameraview.Scanner.TextScanner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class CameraHandler @Inject constructor(private val context: Context,
                                        private val qrScanner: QRScanner,
                                        private val textScanner: TextScanner) {

    private lateinit var camera: Camera
    private lateinit var cameraProvider : ProcessCameraProvider
    private lateinit var cameraExecutor : ExecutorService
    private lateinit var currentAnalyzer: BaseAnalyzer
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var previewView: PreviewView

    init {
        setup()
    }

    private fun setup() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        currentAnalyzer = qrScanner
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
            imageAnalysis.setAnalyzer(cameraExecutor, currentAnalyzer)

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
            camera = cameraProvider.bindToLifecycle(lifecycleOwner,
                    cameraSelector,
                    imageAnalysis,
                    preview)
            preview.setSurfaceProvider(previewView.createSurfaceProvider(camera.cameraInfo))
            previewView.scaleType = PreviewView.ScaleType.FIT_START
        }, ContextCompat.getMainExecutor(context))
    }

    fun switchScanType(currentMode: SCAN_TYPE) : SCAN_TYPE {
        cameraProvider.unbindAll()
        lateinit var newCurrentMode : SCAN_TYPE
        //switch to other scan type
        when (currentMode) {
            SCAN_TYPE.BARCODE -> {
                newCurrentMode = SCAN_TYPE.TEXT
                setTextAnalyzer()
            }
            SCAN_TYPE.TEXT -> {
                newCurrentMode = SCAN_TYPE.BARCODE
                setQrAnalyzer()
            }
        }
        return newCurrentMode
    }

    private fun setQrAnalyzer(){
        currentAnalyzer.close()
        currentAnalyzer = qrScanner
        openCamera(lifecycleOwner,previewView)
    }

    private fun setTextAnalyzer(){
        currentAnalyzer.close()
        currentAnalyzer = textScanner
        openCamera(lifecycleOwner,previewView)
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
