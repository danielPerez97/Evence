package teamevence.evenceapp.qrcameraview.Camera


import android.content.Context
import android.content.res.Resources
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.slider.Slider
import teamevence.evenceapp.qrcameraview.Scanner.BaseAnalyzer
import teamevence.evenceapp.qrcameraview.Scanner.QRScanner
import teamevence.evenceapp.qrcameraview.Scanner.TextScanner
import teamevence.evenceapp.qrcameraview.data.SCAN_TYPE
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraHandler constructor(private val context: Context,
                                        private val qrScanner: QRScanner,
                                        private val textScanner: TextScanner) {

    private lateinit var camera: Camera
    private lateinit var cameraProvider : ProcessCameraProvider
    private lateinit var cameraExecutor : ExecutorService
    private lateinit var currentAnalyzer: BaseAnalyzer
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var previewView: PreviewView
    private lateinit var slider: Slider
    var isPortraitMode : Boolean = true

    init {
        setup()
    }

    private fun setup() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        currentAnalyzer = qrScanner
    }

    fun openCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView, slider: Slider) {
        this.lifecycleOwner = lifecycleOwner
        this.previewView = previewView
        this.slider = slider
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
            setupZoomSlider(camera.cameraControl)
            preview.setSurfaceProvider(previewView.createSurfaceProvider(camera.cameraInfo))
            previewView.scaleType = PreviewView.ScaleType.FIT_CENTER
        }, ContextCompat.getMainExecutor(context))
    }

    fun setupZoomSlider(cameraControl: CameraControl){
        slider.addOnChangeListener { rangeSlider, value, fromUser ->
            cameraControl.setLinearZoom(value/100f)
        }
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
        openCamera(lifecycleOwner,previewView, slider)
    }

    //FOR FUTURE UPDATES
    private fun setTextAnalyzer(){
        currentAnalyzer.close()
        //currentAnalyzer = textScanner
        openCamera(lifecycleOwner,previewView, slider)
    }

    fun toggleFlash(flashOn: Boolean) {
        camera.cameraControl.enableTorch(!flashOn)
    }

    fun getAnalyzedImageSize() : Size {
        val shortSide = Math.min(currentAnalyzer.imageProxy.width,
                currentAnalyzer.imageProxy.height)
        val longSide = Math.max(currentAnalyzer.imageProxy.width,
                currentAnalyzer.imageProxy.height)

        return if (isPortraitMode)
            Size(shortSide, longSide)
        else //landscape (for future updates)
            Size(shortSide, longSide)
    }

    fun unbind(){
        cameraProvider.unbindAll()
    }
}
