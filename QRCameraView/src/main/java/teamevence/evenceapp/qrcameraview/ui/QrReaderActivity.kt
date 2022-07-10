package teamevence.evenceapp.qrcameraview.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mlkit.vision.barcode.Barcode
import dagger.hilt.android.AndroidEntryPoint
import teamevence.evenceapp.core.*
import teamevence.evenceapp.core.db.UiNewEvent
import teamevence.evenceapp.core.db.toViewEvent
import teamevence.evenceapp.qrcameraview.Camera.CameraHandler
import teamevence.evenceapp.qrcameraview.R
import teamevence.evenceapp.qrcameraview.data.SCAN_TYPE
import teamevence.evenceapp.qrcameraview.data.ScannedData
import teamevence.evenceapp.qrcameraview.databinding.ActivityQrReaderBinding
import teamevence.evenceapp.qrcameraview.viewmodel.QrReaderViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@AndroidEntryPoint
class QrReaderActivity : BaseActivity(), DialogClosable
{
    private lateinit var binding: ActivityQrReaderBinding
    private lateinit var viewModel: QrReaderViewModel
    private lateinit var overlays : Overlays
    private lateinit var adapter: ScannedQrAdapter
    private var scannedData: List<ScannedData> = emptyList()
    private var currentScanType: SCAN_TYPE = SCAN_TYPE.BARCODE
    private var flashOn = false
    private var scanOn = true

    @Inject lateinit var dialogStarter: DialogStarter
    @Inject lateinit var cameraHandler: CameraHandler

    private var barcodeTypes = BarcodeTypes(this)

    companion object {
        private const val REQUEST_CAMERA_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityQrReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(QrReaderViewModel::class.java)

        if (allPermissionsGranted()) {
            cameraHandler.openCamera(this, binding.previewView, binding.zoomSlider)
        } else
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSIONS)

        overlays = Overlays(this)
        binding.parentLayout.addView(overlays)
        binding.scanButton.setOnClickListener { onScanClick() }
        //binding.switchScanButton.setOnClickListener { toggleScanMode() }
        binding.flashButton.setOnClickListener { toggleFlash() }

        handleRecyclerView()
        setupSubscriptions()
    }

    private fun handleRecyclerView(){
        adapter = ScannedQrAdapter(this)
        binding.cameraRecyclerView.adapter = adapter
        val llm =  LinearLayoutManager(baseContext)
        llm.stackFromEnd = true
        binding.cameraRecyclerView.layoutManager = llm
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateViews() {
        if (scanOn)
            if (scannedData.isEmpty()) {
                overlays.clearOverlays() //todo rename appropriately
                when (currentScanType) {
                    SCAN_TYPE.BARCODE -> {

                    }
                    SCAN_TYPE.TEXT -> {

                    }
                }
            } else {
                overlays.updateScannedImageSize(cameraHandler.getAnalyzedImageSize())
                binding.result.text = ""
                when (currentScanType) {
                    SCAN_TYPE.BARCODE -> {
                        overlays.updateOverlays(scannedData)
                    }
                }
            }
    }

    private fun onScanClick() {
        when {
            scanOn -> showResults()
            else -> hideResults()
        }
    }

    private fun showResults(){
        scanOn = false
        adapter.setData(scannedData) // todo create child class for scanned data
        overlays.clearOverlays()
        binding.cameraRecyclerView.visibility = View.VISIBLE
        binding.flashButton.visibility = View.GONE
        binding.zoomSlider.visibility = View.GONE
        binding.darkBackground.visibility = View.VISIBLE
        binding.scanButton.setImageDrawable(getDrawable(R.drawable.ic_close_white_24dp))

        if (scannedData.isEmpty()) {
            binding.result.visibility = View.VISIBLE
            binding.result.text = "No QR code found" }
        else
            binding.result.visibility = View.GONE
    }

    private fun hideResults() {
        scanOn = true
        adapter.clearData()
        binding.cameraRecyclerView.visibility = View.GONE
        binding.scanButton.setImageDrawable(getDrawable(R.drawable.ic_search_white_24dp))
        binding.flashButton.visibility = View.VISIBLE
        binding.zoomSlider.visibility = View.VISIBLE
        //binding.switchScanButton.visibility = View.VISIBLE
        binding.darkBackground.visibility = View.GONE
        binding.result.visibility = View.GONE
    }

    private fun handleQrEvent(barcode: Barcode) {
        val uiNewEvent = viewModel.toUINewEvent(barcode.calendarEvent)

        disposables.add(viewModel.saveEvent(uiNewEvent)
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe {
                    dialogStarter.startQrDialog(this, it.toViewEvent())
                })
    }

    private fun toggleFlash() {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            cameraHandler.toggleFlash(flashOn)
            flashOn = !flashOn
            if (!flashOn)
                binding.flashButton.setImageDrawable(getDrawable(R.drawable.ic_flash_off_white_24dp))
            else
                binding.flashButton.setImageDrawable(getDrawable(R.drawable.ic_flash_on_white_24dp))
            //todo use callback to see if successful or not
        } else {
            toastShort(this, "Device torch not found")
        }
    }

    //For future updates
//    private fun toggleScanMode() {
//        currentScanType = cameraHandler.switchScanType(currentScanType)
//        when (currentScanType) {
//            SCAN_TYPE.BARCODE -> {
//                binding.switchScanImageview.setImageDrawable(getDrawable(R.drawable.ic_baseline_qr_code_scanner_24))
//            }
//            SCAN_TYPE.TEXT -> {
//                binding.switchScanImageview.setImageDrawable(getDrawable(R.drawable.ic_baseline_scan_text_24))
//            }
//        }
//    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
                baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraHandler.openCamera(this, binding.previewView, binding.zoomSlider)
            } else {
                toastShort(this, "Permissions not granted by the user.")
                finish()
            }
        }
    }

    private fun setupSubscriptions() {
        disposables.add(viewModel.liveQRData()
                .subscribe {
                    scannedData = it
                    updateViews()
                })
        /*disposables.add(viewModel.liveTextData()
                //add onerrorhandler
                .subscribe {
                    scannedData = it
                    updateViews()
                })
         */
        disposables.add(adapter.clicks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { barcode: Barcode? ->
                    if (barcode != null) {
                        if (barcode.valueType == Barcode.TYPE_CALENDAR_EVENT)
                            handleQrEvent(barcode)
                        else
                            barcodeTypes.performAction(barcode)
                        Log.d("QrReaderActivity--------------", "clicked")
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
        overlays.clearOverlays()
    }

    override fun close() {
        finish()
    }
}
