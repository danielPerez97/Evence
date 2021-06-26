package daniel.perez.qrcameraview.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mlkit.vision.barcode.Barcode
import dagger.hilt.android.AndroidEntryPoint
import daniel.perez.core.*
import daniel.perez.core.db.UiNewEvent
import daniel.perez.core.db.toViewEvent
import daniel.perez.core.service.FileManager
import daniel.perez.qrcameraview.Camera.CameraHandler
import daniel.perez.qrcameraview.IntentActions
import daniel.perez.qrcameraview.R
import daniel.perez.qrcameraview.data.SCAN_TYPE
import daniel.perez.qrcameraview.data.ScannedData
import daniel.perez.qrcameraview.databinding.ActivityQrReaderBinding
import daniel.perez.qrcameraview.viewmodel.QrReaderViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import timber.log.Timber
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
    @Inject lateinit var intentActions: IntentActions
    @Inject lateinit var barcodeTypes: BarcodeTypes

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
            cameraHandler.openCamera(this, binding.previewView)
        } else
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSIONS)

        overlays = Overlays(this)
        binding.parentLayout.addView(overlays)
        binding.scanButton.setOnClickListener { onScanClick() }
        binding.switchScanButton.setOnClickListener { toggleScanMode() }
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
                        binding.result.text = "Scanning for QR codes"
                    }
                    SCAN_TYPE.TEXT -> {
                        binding.result.text = "Scanning for texts"
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
        binding.scanButton.setImageDrawable(getDrawable(R.drawable.ic_close_white_24dp))
        binding.result.text = "Found ${scannedData.size} QR codes" //todo fix plurality
        binding.flashButton.visibility = View.GONE
        binding.switchScanButton.visibility = View.GONE
        binding.darkBackground.visibility = View.VISIBLE
    }

    private fun hideResults() {
        scanOn = true
        adapter.clearData()
        binding.cameraRecyclerView.visibility = View.GONE
        binding.scanButton.setImageDrawable(getDrawable(R.drawable.ic_search_white_24dp))
        binding.flashButton.visibility = View.VISIBLE
        binding.switchScanButton.visibility = View.VISIBLE
        binding.darkBackground.visibility = View.GONE
    }

    private fun handleQrEvent(barcode: Barcode) {
        val uiNewEvent: UiNewEvent
        with (barcode.calendarEvent) {
            uiNewEvent = UiNewEvent(
                    summary,
                    description,
                    location,
                    toLocalDateTime(start.day, start.month, start.year, start.hours, start.minutes),
                    toLocalDateTime(end.day, end.month, end.year, end.hours, end.minutes)
            )
        }

        viewModel.saveEvent(uiNewEvent)
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe {
                    dialogStarter.startQrDialog(this, it.toViewEvent())
                }
    }

    private fun toggleFlash() {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            cameraHandler.toggleFlash(flashOn)
            flashOn = !flashOn
            if (!flashOn)
                binding.flashImage.setImageDrawable(getDrawable(R.drawable.ic_flash_off_white_24dp))
            else
                binding.flashImage.setImageDrawable(getDrawable(R.drawable.ic_flash_on_white_24dp))
            //todo use callback to see if successful or not
        } else {
            toastShort("Device torch not found")
        }
    }

    private fun toggleScanMode() {
        currentScanType = cameraHandler.switchScanType(currentScanType)
        when (currentScanType) {
            SCAN_TYPE.BARCODE -> {
                binding.switchScanImageview.setImageDrawable(getDrawable(R.drawable.ic_baseline_qr_code_scanner_24))
            }
            SCAN_TYPE.TEXT -> {
                binding.switchScanImageview.setImageDrawable(getDrawable(R.drawable.ic_baseline_scan_text_24))
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
                baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraHandler.openCamera(this, binding.previewView)
            } else {
                toastShort("Permissions not granted by the user.")
                finish()
            }
        }
    }

    private fun setupSubscriptions() {
        disposables.add(viewModel.liveQRData()
                .subscribe {
                    Timber.d("Received data from QR Scanner")
                    Timber.d(it.toString())
                    scannedData = it
                    updateViews()
                })
        disposables.add(viewModel.liveTextData()
                //add onerrorhandler
                .subscribe {
                    scannedData = it
                    updateViews()
                })
        disposables.add(adapter.clicks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { barcode: Barcode? ->
                    if (barcode != null) {
                        if (barcode.valueType == Barcode.TYPE_CALENDAR_EVENT)
                            handleQrEvent(barcode)
                        else
                            barcodeTypes.performAction(barcode)
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
