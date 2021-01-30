package daniel.perez.qrcameraview

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import daniel.perez.core.model.ViewCalendarData
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.toZonedDateTime
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec

class QrHandler(
        private val context: Context,
        private val imageProxy: ImageProxy,
        private val image : InputImage) {

    private lateinit var qr : Barcode.CalendarEvent
    init{
        scanBarcode()
    }

    private fun scanBarcode(){
        //Gets instance of BarcodeScanner. where the magic happens
        val barcodeScanner = BarcodeScanning.getClient()
        val result = barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for(barcode in barcodes) {
                        val rawValue = barcode.rawValue
                        Log.i("QRScanner", "raw vavlue=$rawValue" )
                        qr = barcodes[0]
                        receiveQrDetection()
                    }
                }
                .addOnFailureListener{
                    Log.e("QRScanner", "scan failed", it)
                }
                .addOnCompleteListener{
                    imageProxy.close()
                }
    }

    private fun receiveQrDetection() {
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

    fun openQrEventDialog() {
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
}