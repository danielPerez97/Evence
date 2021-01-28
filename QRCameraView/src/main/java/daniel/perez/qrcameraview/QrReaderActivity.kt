package daniel.perez.qrcameraview


import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.View
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import daniel.perez.core.BaseActivity
import daniel.perez.core.DialogStarter
import daniel.perez.core.model.ViewCalendarData
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.service.FileManager
import daniel.perez.core.service.qr.QrBitmapGenerator
import daniel.perez.core.toZonedDateTime
import daniel.perez.core.toastShort
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec
import daniel.perez.qrcameraview.databinding.ActivityQrReaderBinding
import daniel.perez.qrcameraview.di.QrReaderComponentProvider
import daniel.perez.qrcameraview.viewmodel.QrReaderViewModel
import java.util.concurrent.Executors
import javax.inject.Inject

class QrReaderActivity : BaseActivity()//, SurfaceHolder.Callback, Detector.Processor<Barcode>
{
    lateinit var binding: ActivityQrReaderBinding
    private lateinit var viewModel: QrReaderViewModel
    private lateinit var camera : Camera
    private lateinit var qr : Barcode
    private var isScanning = true
    private var flashOn = false

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var generator: QrBitmapGenerator
    @Inject lateinit var fileManager: FileManager
    @Inject lateinit var dialogStarter: DialogStarter

    companion object {
        private val REQUEST_CAMERA_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as QrReaderComponentProvider).getQrReaderComponent().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityQrReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(QrReaderViewModel::class.java)

        if (allPermissionsGranted()) {
           setupCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSIONS)
        }

//        binding.qrTypeCardview.setOnClickListener { clickityClick() }
        //   binding.flashImageview.setOnClickListener { toggleFlash() }
    }

    //
    fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val cameraExecutor = Executors.newSingleThreadExecutor()

        cameraProviderFuture.addListener(Runnable {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)

            //provides CPU-accessible buffers for analysis, such as for machine learning.
            val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(displayMetrics.widthPixels, displayMetrics.heightPixels))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
            imageAnalysis.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { imageProxy -> analyzeImage(imageProxy) })

            //sets up surface for displaying the image preview
            val preview = Preview.Builder()
                    .build()
            preview.setSurfaceProvider(binding.previewView.createSurfaceProvider(null))

            //chooses a camera
            val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
            val cameraProvider = cameraProviderFuture.get() ?: throw IllegalStateException("Camera initialization failed.")
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)

        }, ContextCompat.getMainExecutor(this))
    }

    //analyzes the image and reads the barcode
    @SuppressLint("UnsafeExperimentalUsageError")
    private fun analyzeImage(imageProxy: ImageProxy) {
        val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_AZTEC,
                        Barcode.FORMAT_UPC_A,
                        Barcode.FORMAT_UPC_E
                )
                .build()
        val mediaImage = imageProxy.image

        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            //Gets instance of BarcodeScanner. where the magic happens
            val barcodeScanner = BarcodeScanning.getClient()
            val result = barcodeScanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        for(barcode in barcodes) {
                            val rawValue = barcode.rawValue
                            Log.i("QRScanner", "raw vavlue=$rawValue" )
                            receiveQRDetection(barcodes)
                        }
                    }
                    .addOnFailureListener{
                        Log.e("QRScanner", "scan failed", it)
                    }
                    .addOnCompleteListener{
                        imageProxy.close()
                    }
        }
    }

    private fun receiveQRDetection(barcodes: MutableList<Barcode>) {
        binding.result.post( Runnable {
            if (barcodes.size != 0) {
                isScanning = false

                qr = barcodes[0]
                qr.let {
                    binding.result.text = qr.displayValue
                    when (qr.valueType) {
                        Barcode.TYPE_CALENDAR_EVENT ->
                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_event_white_36dp))
                        Barcode.TYPE_URL ->
                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_open_in_new_white_24dp))
                        Barcode.TYPE_CONTACT_INFO ->
                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_person_add_white_24dp))
                        Barcode.TYPE_EMAIL ->
                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_email_white_24dp))
                        Barcode.TYPE_PHONE ->
                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_phone_white_24dp))
                        Barcode.TYPE_SMS ->
                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_textsms_black_24dp))
                        Barcode.TYPE_ISBN ->
                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_shopping_cart_white_24dp))
                        Barcode.TYPE_WIFI -> {
                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_wifi_white_24dp))
                            binding.result.text = "Network name: " + qr.wifi.ssid + "\nPassword: " + qr.wifi.password
                        }
                        Barcode.TYPE_GEO ->
                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_place_white_24dp))
                        Barcode.TYPE_DRIVER_LICENSE ->
                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_account_box_white_24dp))
                        else ->
                            binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_short_text_white_24dp))
                    }
                }

            } else {
                isScanning = true
                binding.qrTypeImageview.setImageDrawable(getDrawable(R.drawable.ic_search_white_24dp))
                binding.result.text = "Scanning..."
            }
        })
    }

    fun onQRClick(view : View) {
        //Log.d("QrReaderActivity", "raw value= " + qr.rawValue )
        if (isScanning) {
            //Toast.makeText(this, "No QR code found", Toast.LENGTH_SHORT).show()
        } else {
            //Toast.makeText(this, qr.rawValue, Toast.LENGTH_SHORT).show()

            when (qr.valueType) {
                Barcode.TYPE_CALENDAR_EVENT -> {
                    openQrEventDialog(qr.calendarEvent)
                }
                Barcode.TYPE_URL -> {
                    val webpage: Uri = Uri.parse(qr.url.url)
                    val intent = Intent(Intent.ACTION_VIEW, webpage)
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                }
                Barcode.TYPE_CONTACT_INFO -> {
                    val intent = Intent(Intent.ACTION_INSERT).apply {
                        type = ContactsContract.Contacts.CONTENT_TYPE
                        putExtra(ContactsContract.Intents.Insert.NAME, qr.contactInfo.name?.formattedName)
                        putExtra(ContactsContract.Intents.Insert.EMAIL, qr.contactInfo.emails?.get(0)?.address)
                        putExtra(ContactsContract.Intents.Insert.PHONE, qr.contactInfo.phones?.get(0)?.number)
                        putExtra(ContactsContract.Intents.Insert.COMPANY, qr.contactInfo.organization?.toString())
                    }
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                }
                Barcode.TYPE_EMAIL -> {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, listOf(qr.email.address).toTypedArray())
                        putExtra(Intent.EXTRA_SUBJECT, qr.email.subject)
                        putExtra(Intent.EXTRA_TEXT, qr.email.body)
                    }
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                }
                Barcode.TYPE_PHONE -> {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:" + qr.phone.number)
                    }
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                }
                Barcode.TYPE_SMS -> {
                    toastShort(qr.sms.phoneNumber)
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("smsto: " + qr.sms.phoneNumber)  // This ensures only SMS apps respond
                        putExtra("sms_body", qr.sms.message)
                        putExtra("exit_on_sent", true)
                    }
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                }
                Barcode.TYPE_ISBN -> {
                    val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                        putExtra(SearchManager.QUERY, qr.displayValue)
                    }
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                }
                Barcode.TYPE_WIFI -> {
                    val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager;
                    val clip = ClipData.newPlainText("wifi password", qr.wifi.password);
                    clipboard.setPrimaryClip(clip)
                    toastShort("Password copied")
                }
                Barcode.TYPE_GEO -> {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(qr.geoPoint.toString())
                    }
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                }
            }
        }
    }

    fun openQrEventDialog(qr: Barcode.CalendarEvent) {
        // Handle the dates
        //val startMonthDayYear: IntArray = intArrayOf(qr.start.month, qr.start.day, qr.start.year)
        //val endMonthDayYear: IntArray =  intArrayOf(qr.end.month, qr.end.day, qr.end.year)

        // Handle the hours and minutes
        //val startHourMinutes: IntArray =  intArrayOf(qr.start.hours, qr.start.minutes)
        //val endHourMinutes: IntArray = intArrayOf(qr.end.hours, qr.end.minutes)

        val event = EventSpec.Builder(0)
                .title(qr.summary)
                .description(qr.description)
                .location(qr.location)
                .start(toZonedDateTime(qr.start.month, qr.start.day, qr.start.year, qr.start.hours, qr.start.minutes))
                .end(toZonedDateTime(qr.end.month, qr.end.day, qr.end.year, qr.end.hours, qr.end.minutes))
                .build()

        val currentEvent = ICalSpec.Builder()
                .fileName(qr.summary)
                .addEvent(event)
                .build()

        // Write the file to the file system
        viewModel.saveFile(currentEvent)
        val viewEvent = ViewEvent(event.title,
                event.description,
                event.getStartDate(),
                event.getStartTime(),
                event.getStartInstantEpoch(),
                event.getEndEpochMilli(),
                event.location,
                event.text(),
                generator.forceGenerate(event.text())
        )
        val calendar = ViewCalendarData(currentEvent.fileName, listOf(viewEvent))
        dialogStarter.startQrDialog(this, calendar)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
                baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSIONS) {
            if (allPermissionsGranted()) {
                setupCamera()
            } else {
                toastShort("Permissions not granted by the user.")
                finish()
            }
        }
    }


    fun toggleFlash(view : View){
        //todo fix flash
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {

            if (!flashOn) {
                binding.flashImageview.setImageDrawable(getDrawable(R.drawable.ic_flash_off_white_24dp))
                flashOn = true
            } else {
                binding.flashImageview.setImageDrawable(getDrawable(R.drawable.ic_flash_on_white_24dp))
                flashOn = false
            }
            camera.cameraControl.enableTorch(!flashOn)
            //todo use callback to see if successful or not
        } else {
           toastShort("Device torch not found")
        }
    }


}
