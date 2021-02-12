package daniel.perez.qrcameraview.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.text.Text
import daniel.perez.core.BaseActivity
import daniel.perez.core.DialogStarter
import daniel.perez.core.copyToClipboard
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
import timber.log.Timber
import javax.inject.Inject

class QrReaderActivity : BaseActivity() {
    private lateinit var binding: ActivityQrReaderBinding
    private lateinit var viewModel: QrReaderViewModel
    private var flashOn = false
    private lateinit var scannedData: List<ScannedData>
    private  var qrBoundingBoxes: List<Rect>  = emptyList()
    private  var textBoundingBoxes: List<Rect?> = emptyList()
    private lateinit var intentActions: IntentActions
    private lateinit var outlineOverlay: OutlineOverlay

    private var currentScanType: SCAN_TYPE = SCAN_TYPE.BARCODE

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

        if (allPermissionsGranted()) {
            cameraHandler.openCamera(this, binding.previewView)
        } else
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSIONS)

        outlineOverlay = OutlineOverlay(this, binding)
        binding.parentLayout.addView(outlineOverlay)

        binding.qrTypeCardview.setOnClickListener { onQRClick() }
        binding.switchScanButton.setOnClickListener { toggleScanMode() }
        binding.flashButton.setOnClickListener { toggleFlash() }


        binding.textSwitcher.setInAnimation(this, android.R.anim.slide_in_left)
        binding.textSwitcher.setOutAnimation(this, android.R.anim.slide_out_right)
        setupSubscriptions()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateViews() {
        if (isScanning()) {
            binding.qrTypeCardview.setImageDrawable(getDrawable(R.drawable.ic_search_white_24dp))
            outlineOverlay.clearOverlays()
            when (currentScanType) {
                SCAN_TYPE.BARCODE -> {
                    binding.result.text = "Scanning for QR codes"
                }
                SCAN_TYPE.TEXT -> {
                    binding.result.text = "Scanning for texts"
                }
            }
        } else {
            when (currentScanType) {
                SCAN_TYPE.BARCODE -> {
                    val qrData = scannedData[0].data as Barcode
                    binding.result.text = qrData.displayValue
                    outlineOverlay.addOverlay(scannedData)
                    binding.qrTypeCardview.setImageDrawable(outlineOverlay.setBarcodeTypeIcon(qrData))
                }

                SCAN_TYPE.TEXT -> {
                    val textBlock = scannedData[0].data as Text.TextBlock
                    binding.result.text = textBlock.text
                    //outlineOverlay.addOverlay(textBoundingBoxes)
                }
            }
        }
    }

    fun onQRClick() {
        if (isScanning()) {
            toastShort("No QR code found")
        } else {
            val qrData = scannedData[0].data as Barcode
            Timber.i(qrData.valueType.toString())
            when (qrData.valueType) {
                Barcode.TYPE_CALENDAR_EVENT -> handleQrEvent()
                Barcode.TYPE_URL -> intentActions.openWebpage(qrData.url)
                Barcode.TYPE_CONTACT_INFO -> intentActions.saveContact(qrData.contactInfo)
                Barcode.TYPE_EMAIL -> intentActions.sendEmail(qrData.email)
                Barcode.TYPE_PHONE -> intentActions.performCall(qrData.phone)
                Barcode.TYPE_SMS -> intentActions.sendSMS(qrData.sms)
                Barcode.TYPE_ISBN -> intentActions.searchWeb(qrData)
                Barcode.TYPE_WIFI -> copyToClipboard(this, "wifi password", qrData.wifi.password)
                Barcode.TYPE_GEO -> intentActions.searchGeo(qrData.geoPoint)
                else -> copyToClipboard(this, "Copy text", qrData.displayValue)
            }
        }
    }

    private fun handleQrEvent() {
        val qrData = scannedData[0].data as Barcode
        val currentEvent = ICalSpec.Builder()
                .fileName(qrData.calendarEvent.summary)
                .addEvent(viewModel.toEventSpec(qrData))
                .build()

        // Write the file to the file system
        viewModel.saveFile(currentEvent)
        val calendar = ViewCalendarData(currentEvent.fileName,
                listOf(viewModel.toViewEvent(viewModel.toEventSpec(qrData))))
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
        //switches scan type
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

    private fun isScanning(): Boolean = scannedData.isEmpty()


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
        disposables.add(viewModel.liveQRBoundingBoxes()
                .subscribe {
                    qrBoundingBoxes = it
                    updateViews()
                })
        disposables.add(viewModel.liveTextData()
                //add onerrorhandler
                .subscribe {
                    scannedData = it
                    updateViews()
                })
        disposables.add(viewModel.liveTextBoundingBoxes()
                .subscribe {
                    textBoundingBoxes = it
                    updateViews()
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
        outlineOverlay.clearOverlays()
    }
}
