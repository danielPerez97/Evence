package daniel.perez.qrcameraview

import android.Manifest
import android.app.SearchManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.DisplayMetrics
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
//import com.google.android.gms.vision.CameraSource
//import com.google.android.gms.vision.Detector
//import com.google.android.gms.vision.barcode.Barcode
//import com.google.android.gms.vision.barcode.BarcodeDetector
import daniel.perez.core.DialogStarter
import daniel.perez.core.model.ViewCalendarData
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.service.FileManager
import daniel.perez.core.service.qr.QrBitmapGenerator
import daniel.perez.core.toZonedDateTime
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec
import daniel.perez.qrcameraview.databinding.ActivityQrReaderBinding
import daniel.perez.qrcameraview.di.QrReaderComponentProvider
import daniel.perez.qrcameraview.viewmodel.QrReaderViewModel
import java.io.IOException
import javax.inject.Inject

class QrReaderActivity : AppCompatActivity()//, SurfaceHolder.Callback, Detector.Processor<Barcode>
{
    lateinit var binding: ActivityQrReaderBinding
    private lateinit var viewModel : QrReaderViewModel
//    private lateinit var cameraSource: CameraSource
//    private var qr = Barcode()
    private var isScanning = true
    private var flashOn = false
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var generator: QrBitmapGenerator
    @Inject lateinit var fileManager: FileManager
    @Inject lateinit var dialogStarter: DialogStarter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        (application as QrReaderComponentProvider).getQrReaderComponent().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityQrReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(QrReaderViewModel::class.java)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraId = cameraManager.cameraIdList[0]
        } catch (e : CameraAccessException) {
            e.printStackTrace()
        }

//        binding.qrTypeCardview.setOnClickListener { clickityClick() }
        binding.flashImageview.setOnClickListener { toggleFlash() }
    }

//    override fun onResume(){
//        super.onResume()
//        setupCameraAndReader()
//    }

//    override fun onPause() {
//        super.onPause()
//        cameraSource.release()
//    }
//
//    private fun setupCameraAndReader(){
//        val barcodeDetector: BarcodeDetector = BarcodeDetector.Builder(this)
//                .setBarcodeFormats(Barcode.ALL_FORMATS)
//                .build()
//
//        val displayMetrics = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(displayMetrics)
//
//        cameraSource = CameraSource.Builder(this, barcodeDetector)
//                .setRequestedPreviewSize(displayMetrics.heightPixels,displayMetrics.widthPixels)
//                .setAutoFocusEnabled(true)
//                .build()
//        binding.cameraSurfaceView.holder.addCallback(this)
//        barcodeDetector.setProcessor(this)
//    }
//
//    companion object {
//        const val CAMERA_REQUEST_CODE = 101
//    }
//
//    override fun surfaceCreated(p0: SurfaceHolder?) {
//        try {
//            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                cameraSource.start(binding.cameraSurfaceView.holder)
//            } else {
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
//    }
//
//    override fun surfaceDestroyed(p0: SurfaceHolder?) {
//        cameraSource.stop()
//    }
//
//    override fun release() {
//    }
//
//    override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
//        val barcodeArray = detections?.detectedItems
//
//        binding.result.post( Runnable {
//            if (barcodeArray?.size() != 0) {
//                isScanning = false
//                qr = barcodeArray?.valueAt(0)!!
//
//                qr.let {
//                    binding.result.text = qr.displayValue
//
//                    when (qr.valueFormat) {
//                        Barcode.CALENDAR_EVENT ->
//                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_event_white_36dp))
//                        Barcode.URL ->
//                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_open_in_new_white_24dp))
//                        Barcode.CONTACT_INFO ->
//                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_person_add_white_24dp))
//                        Barcode.EMAIL ->
//                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_email_white_24dp))
//                        Barcode.PHONE ->
//                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_phone_white_24dp))
//                        Barcode.SMS ->
//                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_textsms_black_24dp))
//                        Barcode.ISBN ->
//                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_shopping_cart_white_24dp))
//                        Barcode.WIFI -> {
//                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_wifi_white_24dp))
//                            binding.result.text = "Network name: " + qr.wifi.ssid + "\nPassword: " + qr.wifi.password
//                        }
//                        Barcode.GEO ->
//                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_place_white_24dp))
//                        Barcode.DRIVER_LICENSE ->
//                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_account_box_white_24dp))
//                        else ->
//                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_short_text_white_24dp))
//                    }
//                }
//
//            } else {
//                isScanning = true
//                binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_search_white_24dp))
//                binding.result.text = "Scanning..."
//            }
//
//            animate()
//        })
//    }
//
//    fun openQrEventDialog(qr : Barcode.CalendarEvent){
//        // Handle the dates
//        val startMonthDayYear: IntArray = intArrayOf(qr.start.month, qr.start.day, qr.start.year)
//        val endMonthDayYear: IntArray =  intArrayOf(qr.end.month, qr.end.day, qr.end.year)
//
//        // Handle the hours and minutes
//        val startHourMinutes: IntArray =  intArrayOf(qr.start.hours, qr.start.minutes)
//        val endHourMinutes: IntArray = intArrayOf(qr.end.hours, qr.end.minutes)
//
//        val event = EventSpec.Builder(0)
//                .title(qr.summary)
//                .description(qr.description)
//                .location(qr.location)
//                .start(toZonedDateTime(startMonthDayYear, startHourMinutes))
//                .end(toZonedDateTime(endMonthDayYear, endHourMinutes))
//                .build()
//
//        val currentEvent = ICalSpec.Builder()
//                .fileName(qr.summary)
//                .addEvent(event)
//                .build()
//
//        // Write the file to the file system
//        viewModel.saveFile(currentEvent)
//        val viewEvent = ViewEvent(event.title,
//                event.description,
//                event.getStartTime(),
//                event.getStartDate(),
//                event.getStartInstantEpoch(),
//                event.getEndEpochMilli(),
//                event.location,
//                event.text(),
//                generator.forceGenerate(event.text())
//        )
//        val calendar = ViewCalendarData(currentEvent.fileName, listOf(viewEvent))
//        dialogStarter.startQrDialog(calendar)
//    }
//
//    fun clickityClick(){
//        Log.d("QrReaderActivity", "raw value= " + qr.rawValue )
//        if (isScanning) {
//            Toast.makeText(this, "No QR code found", Toast.LENGTH_SHORT).show()
//        } else {
//            //Toast.makeText(this, qr.rawValue, Toast.LENGTH_SHORT).show()
//
//            when(qr.valueFormat) {
//                Barcode.CALENDAR_EVENT -> {
//                    openQrEventDialog(qr.calendarEvent)
//                }
//                Barcode.URL -> {
//                    val webpage: Uri = Uri.parse(qr.url.url)
//                    val intent = Intent(Intent.ACTION_VIEW, webpage)
//                    if (intent.resolveActivity(packageManager) != null) {
//                        startActivity(intent)
//                    }
//                }
//                Barcode.CONTACT_INFO -> {
//                    val intent = Intent(Intent.ACTION_INSERT).apply {
//                        type = ContactsContract.Contacts.CONTENT_TYPE
//                        putExtra(ContactsContract.Intents.Insert.NAME, qr.contactInfo.name?.formattedName)
//                        putExtra(ContactsContract.Intents.Insert.EMAIL, qr.contactInfo.emails?.get(0)?.address)
//                        putExtra(ContactsContract.Intents.Insert.PHONE, qr.contactInfo.phones?.get(0)?.number)
//                        putExtra(ContactsContract.Intents.Insert.COMPANY, qr.contactInfo.organization?.toString())
//                    }
//                    if (intent.resolveActivity(packageManager) != null) {
//                        startActivity(intent)
//                    }
//                }
//                Barcode.EMAIL -> {
//                    val intent = Intent(Intent.ACTION_SENDTO).apply {
//                        data = Uri.parse("mailto:")
//                        putExtra(Intent.EXTRA_EMAIL, listOf(qr.email.address).toTypedArray())
//                        putExtra(Intent.EXTRA_SUBJECT, qr.email.subject)
//                        putExtra(Intent.EXTRA_TEXT, qr.email.body)
//                    }
//                    if (intent.resolveActivity(packageManager) != null) {
//                        startActivity(intent)
//                    }
//                }
//                Barcode.PHONE -> {
//                    val intent = Intent(Intent.ACTION_DIAL).apply {
//                        data = Uri.parse("tel:" + qr.phone.number)
//                    }
//                    if (intent.resolveActivity(packageManager) != null) {
//                        startActivity(intent)
//                    }
//                }
//                Barcode.SMS -> {
//                    Toast.makeText(this, qr.sms.phoneNumber, Toast.LENGTH_SHORT).show()
//                    val intent = Intent(Intent.ACTION_SENDTO).apply {
//                        data = Uri.parse("smsto: " + qr.sms.phoneNumber)  // This ensures only SMS apps respond
//                        putExtra("sms_body", qr.sms.message)
//                        putExtra("exit_on_sent", true)
//                    }
//                    if (intent.resolveActivity(packageManager) != null) {
//                        startActivity(intent)
//                    }
//                }
//                Barcode.ISBN -> {
//                    val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
//                        putExtra(SearchManager.QUERY, qr.displayValue)
//                    }
//                    if (intent.resolveActivity(packageManager) != null) {
//                        startActivity(intent)
//                    }
//                }
//                Barcode.WIFI -> {
//                    val clipboard =  getSystemService(CLIPBOARD_SERVICE) as ClipboardManager;
//                    val clip = ClipData.newPlainText("wifi password", qr.wifi.password);
//                    clipboard.setPrimaryClip(clip)
//
//                    Toast.makeText(this, "Password copied", Toast.LENGTH_SHORT).show()
//                }
//                Barcode.GEO -> {
//                    val intent = Intent(Intent.ACTION_VIEW).apply {
//                        data = Uri.parse(qr.geoPoint.toString())
//                    }
//                    if (intent.resolveActivity(packageManager) != null) {
//                        startActivity(intent)
//                    }
//                }
//            }
//        }
//    }

    fun toggleFlash(){
        //todo fix flash
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            if (!flashOn) {
                binding.flashImageview.setImageDrawable(getDrawable(R.drawable.ic_flash_off_white_24dp))
                flashOn = true
            } else {
                binding.flashImageview.setImageDrawable(getDrawable(R.drawable.ic_flash_on_white_24dp))
                flashOn = false
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cameraManager.setTorchMode(cameraId, flashOn)
                }
            } catch (e : CameraAccessException) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "Device torch not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun animate(){
       /* val centerX = binding.qrTypeCardview.width / 2
        val centerY = binding.qrTypeCardview.height / 2
        val radius = Math.hypot(centerX.toDouble(), centerY.toDouble()).toFloat()
        val shrinkAnim = ViewAnimationUtils.createCircularReveal(binding.qrTypeCardview, centerX, centerY, radius, 0.0F)
        val growAnim = ViewAnimationUtils.createCircularReveal(binding.qrTypeCardview, centerX, centerY, radius, 0.0F)

        shrinkAnim.start()
        growAnim.start()*/



    }


}
