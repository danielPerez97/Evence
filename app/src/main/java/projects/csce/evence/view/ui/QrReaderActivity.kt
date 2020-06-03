package projects.csce.evence.view.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import projects.csce.evence.R
import projects.csce.evence.databinding.ActivityQrReaderBinding
import projects.csce.evence.ical.EventSpec
import projects.csce.evence.ical.ICalSpec
import projects.csce.evence.service.model.FileManager
import projects.csce.evence.service.model.qr.QrBitmapGenerator
import projects.csce.evence.utils.getAppComponent
import projects.csce.evence.utils.toZonedDateTime
import java.io.IOException
import javax.inject.Inject

class QrReaderActivity : AppCompatActivity(), SurfaceHolder.Callback, Detector.Processor<Barcode> {
    lateinit var binding: ActivityQrReaderBinding
    private lateinit var cameraSource: CameraSource

    @Inject lateinit var generator: QrBitmapGenerator
    @Inject lateinit var fileManager: FileManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_reader)
        binding.view = this
        binding.lifecycleOwner = this
    }

    override fun onResume(){
        super.onResume()
        setupReader()
    }

    override fun onPause() {
        super.onPause()
        cameraSource.release()
    }

    private fun setupReader(){
        val barcodeDetector: BarcodeDetector = BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1080,1080)
                .setAutoFocusEnabled(true)
                .build()

        binding.cameraSurfaceView.holder.addCallback(this)
        barcodeDetector.setProcessor(this)
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 101
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        try {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(binding.cameraSurfaceView.holder)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        cameraSource.stop()
    }

    override fun release() {
    }

    override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
        val barcodeArray = detections?.detectedItems
        if(barcodeArray?.size() != 0 ) {
            binding.result.post( Runnable {
                val qr = barcodeArray?.valueAt(0)
                if (qr?.valueFormat == Barcode.CALENDAR_EVENT)
                    openQrEventDialog(qr.calendarEvent)
                else
                    binding.result.text = qr?.displayValue ?: "No detections"
            })
        }
    }

    fun openQrEventDialog(qr : Barcode.CalendarEvent){
        // Handle the dates
        val startMonthDayYear: IntArray = intArrayOf(qr.start.month, qr.start.day, qr.start.year)
        val endMonthDayYear: IntArray =  intArrayOf(qr.end.month, qr.end.day, qr.end.year)

        // Handle the hours and minutes
        val startHourMinutes: IntArray =  intArrayOf(qr.start.hours, qr.start.minutes)
        val endHourMinutes: IntArray = intArrayOf(qr.end.hours, qr.end.minutes)

        val event = EventSpec.Builder(0)
                .title(qr.summary)
                .description(qr.description)
                .location(qr.location)
                .start(toZonedDateTime(startMonthDayYear, startHourMinutes))
                .end(toZonedDateTime(endMonthDayYear, endHourMinutes))
                .build()

        val currentEvent = ICalSpec.Builder()
                .fileName(qr.summary)
                .addEvent(event)
                .build()


        // Write the file to the file system
        // todo: create viewmodel
        //viewModel.saveFile(currentEvent)

        val qrDialog = QRDialog(this, currentEvent, generator, fileManager)
    }


}
