package daniel.perez.qrcameraview.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mlkit.vision.barcode.Barcode
import daniel.perez.core.BaseActivity
import daniel.perez.core.DialogStarter
import daniel.perez.core.model.ViewCalendarData
import daniel.perez.core.service.FileManager
import daniel.perez.core.toastShort
import daniel.perez.ical.ICalSpec
import daniel.perez.qrcameraview.Camera.CameraHandler
import daniel.perez.qrcameraview.IntentActions
import daniel.perez.qrcameraview.R
import daniel.perez.qrcameraview.Scanner.QRScanner
import daniel.perez.qrcameraview.data.SCAN_TYPE
import daniel.perez.qrcameraview.data.ScannedData
import daniel.perez.qrcameraview.databinding.ActivityQrReaderBinding
import daniel.perez.qrcameraview.di.QrReaderComponentProvider
import daniel.perez.qrcameraview.viewmodel.QrReaderViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class QrReaderActivity : BaseActivity() {
    private lateinit var binding: ActivityQrReaderBinding
    private lateinit var viewModel: QrReaderViewModel
    private lateinit var overlays : Overlays
    private lateinit var adapter: ScannedQrAdapter
    private lateinit var intentActions: IntentActions
    private lateinit var barcodeTypes: BarcodeTypes
    private var scannedData: List<ScannedData> = emptyList()
    private var currentScanType: SCAN_TYPE = SCAN_TYPE.BARCODE
    private var flashOn = false
    private var scanOn = true

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var fileManager: FileManager
    @Inject lateinit var dialogStarter: DialogStarter
    @Inject lateinit var cameraHandler: CameraHandler
    @Inject lateinit var QRScanner: QRScanner

    companion object {
        private const val REQUEST_CAMERA_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as QrReaderComponentProvider).getQrReaderComponent().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityQrReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(QrReaderViewModel::class.java)
        intentActions = IntentActions(this)
        barcodeTypes = BarcodeTypes(this)

        if (allPermissionsGranted()) {
            cameraHandler.openCamera(this, binding.previewView)
        } else
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSIONS)

        overlays = Overlays(this)
        binding.parentLayout.addView(overlays)
        binding.scanButton.setOnClickListener { onScanClick() }
        binding.switchScanButton.setOnClickListener { toggleScanMode() }
        binding.flashButton.setOnClickListener { toggleFlash() }
        binding.textSwitcher.setInAnimation(this, android.R.anim.slide_in_left)
        binding.textSwitcher.setOutAnimation(this, android.R.anim.slide_out_right)

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
                    SCAN_TYPE.TEXT -> {
//                        val textBlock = scannedData[0].data as Text.TextBlock
//                        binding.result.text = textBlock.text
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
    }

    private fun hideResults() {
        scanOn = true
        adapter.clearData()
        binding.cameraRecyclerView.visibility = View.GONE
        binding.scanButton.setImageDrawable(getDrawable(R.drawable.ic_search_white_24dp))
        binding.flashButton.visibility = View.VISIBLE
        binding.switchScanButton.visibility = View.VISIBLE

    }

    private fun handleQrEvent(barcode: Barcode) {
        val currentEvent = ICalSpec.Builder()
                .fileName(barcode.calendarEvent.summary)
                .addEvent(viewModel.toEventSpec(barcode))
                .build()

        // Write the file to the file system
        viewModel.saveFile(currentEvent)
        val calendar = ViewCalendarData(currentEvent.fileName,
                listOf(viewModel.toViewEvent(viewModel.toEventSpec(barcode))))
        dialogStarter.startQrDialog(this, calendar)
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
}
