package daniel.perez.qrcameraview

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.mlkit.vision.barcode.Barcode
import daniel.perez.core.BaseActivity
import daniel.perez.core.DialogStarter
import daniel.perez.core.copyToClipboard
import daniel.perez.core.model.ViewCalendarData
import daniel.perez.core.service.FileManager
import daniel.perez.core.toastShort
import daniel.perez.ical.ICalSpec
import daniel.perez.qrcameraview.databinding.ActivityQrReaderBinding
//import daniel.perez.qrcameraview.databinding.ActivityQrReaderBinding
import daniel.perez.qrcameraview.di.QrReaderComponentProvider
import daniel.perez.qrcameraview.viewmodel.QrReaderViewModel
import timber.log.Timber
import javax.inject.Inject

class QrReaderActivity : BaseActivity() {
    private lateinit var binding: ActivityQrReaderBinding
    private lateinit var viewModel: QrReaderViewModel
    private var flashOn = false
    private lateinit var barcodes : MutableList<Barcode>
    private lateinit var intentActions: IntentActions
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var fileManager: FileManager
    @Inject lateinit var dialogStarter: DialogStarter
    @Inject lateinit var qrHandler: QrHandler
    @Inject lateinit var cameraHandler: CameraHandler

    companion object {
        private const val REQUEST_CAMERA_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as QrReaderComponentProvider).getQrReaderComponent().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityQrReaderBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        viewModel = ViewModelProviders.of(this, viewModelFactory).get(QrReaderViewModel::class.java)
//        intentActions = IntentActions(this)
//        setupSubscriptions()
//
//        if (allPermissionsGranted()) {
//            cameraHandler.setupCamera(this, binding.previewView)
//        } else
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSIONS)
//
//        binding.qrTypeCardview.setOnClickListener { onQRClick() }
//        binding.flashImageview.setOnClickListener { toggleFlash() }
    }
//
//    @SuppressLint("UseCompatLoadingForDrawables")
//    private fun updateViews() {
//        if (isScanning()) {
//            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_search_white_24dp))
//            binding.result.text = "Scanning"
//        } else {
//            val qrData = barcodes[0]
//            binding.result.text = qrData.displayValue
//
//            val anim = BarcodeOverlay(this,qrData.boundingBox)
//
//            when (qrData.valueType) {
//                Barcode.TYPE_CALENDAR_EVENT -> binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_event_white_36dp))
//                Barcode.TYPE_URL -> binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_open_in_new_white_24dp))
//                Barcode.TYPE_CONTACT_INFO -> binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_person_add_white_24dp))
//                Barcode.TYPE_EMAIL -> binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_email_white_24dp))
//                Barcode.TYPE_PHONE -> binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_phone_white_24dp))
//                Barcode.TYPE_SMS -> binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_textsms_black_24dp))
//                Barcode.TYPE_ISBN -> binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_shopping_cart_white_24dp))
//                Barcode.TYPE_WIFI -> {
//                    binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_wifi_white_24dp))
//                    binding.result.text = "Network name: ${qrData.wifi.ssid} \n Password: ${qrData.wifi.password}"
//                }
//                Barcode.TYPE_GEO -> binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_place_white_24dp))
//                Barcode.TYPE_DRIVER_LICENSE -> binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_account_box_white_24dp))
//                else -> binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_short_text_white_24dp))
//            }
//        }
//    }
//
//
//    fun onQRClick() {
//        if (isScanning()) {
//            toastShort("No QR code found")
//        } else {
//            val qrData = barcodes[0]
//            Timber.i(qrData.valueType.toString())
//            when (qrData.valueType) {
//                Barcode.TYPE_CALENDAR_EVENT ->  handleQrEvent()
//                Barcode.TYPE_URL ->   intentActions.openWebpage(qrData.url)
//                Barcode.TYPE_CONTACT_INFO -> intentActions.saveContact(qrData.contactInfo)
//                Barcode.TYPE_EMAIL -> intentActions.sendEmail(qrData.email)
//                Barcode.TYPE_PHONE -> intentActions.performCall(qrData.phone)
//                Barcode.TYPE_SMS -> intentActions.sendSMS(qrData.sms)
//                Barcode.TYPE_ISBN -> intentActions.searchWeb(qrData)
//                Barcode.TYPE_WIFI -> copyToClipboard(this,"wifi password", qrData.wifi.password)
//                Barcode.TYPE_GEO -> intentActions.searchGeo(qrData.geoPoint)
//                else -> copyToClipboard(this,"Copy text", qrData.displayValue)
//            }
//        }
//    }
//
//    private fun handleQrEvent(){
//        val qrData = barcodes[0]
//        val currentEvent = ICalSpec.Builder()
//                .fileName(qrData.calendarEvent.summary)
//                .addEvent(viewModel.toEventSpec(qrData))
//                .build()
//
//        // Write the file to the file system
//        viewModel.saveFile(currentEvent)
//        val calendar = ViewCalendarData(currentEvent.fileName,
//                listOf(viewModel.toViewEvent(viewModel.toEventSpec(qrData))))
//        dialogStarter.startQrDialog(this, calendar)
//    }
//
//    private fun toggleFlash(){
//        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
//            if (!flashOn) {
//                binding.flashImageview.setImageDrawable(getDrawable(R.drawable.ic_flash_off_white_24dp))
//                flashOn = true
//            } else {
//                binding.flashImageview.setImageDrawable(getDrawable(R.drawable.ic_flash_on_white_24dp))
//                flashOn = false
//            }
//            cameraHandler.toggleFlash(flashOn)
//            //todo use callback to see if successful or not
//        } else {
//            toastShort("Device torch not found")
//        }
//    }
//
//    private fun isScanning(): Boolean = barcodes.isEmpty()
//
//    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
//        ContextCompat.checkSelfPermission(
//                baseContext, it) == PackageManager.PERMISSION_GRANTED
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        if (requestCode == REQUEST_CAMERA_PERMISSIONS) {
//            if (allPermissionsGranted()) {
//                cameraHandler.setupCamera(this, binding.previewView)
//            } else {
//                toastShort("Permissions not granted by the user.")
//                finish()
//            }
//        }
//    }
//
//    private fun setupSubscriptions() {
//        disposables.add(viewModel.liveQRData()
//                .subscribe{
//                    barcodes = it
//                    updateViews()
//                }
//        )
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        disposables.dispose()
//    }
}
