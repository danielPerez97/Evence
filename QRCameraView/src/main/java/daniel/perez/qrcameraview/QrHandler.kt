package daniel.perez.qrcameraview

import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import daniel.perez.core.toZonedDateTime
import daniel.perez.ical.EventSpec
import io.reactivex.rxjava3.core.Observable

class QrHandler(
        private val imageProxy: ImageProxy,
        private val image : InputImage) {

    private lateinit var qr : Barcode

    init{
        scanBarcode()
    }

    private fun scanBarcode(){
        //Gets instance of BarcodeScanner. where the magic happens
        val barcodeScanner = BarcodeScanning.getClient()
        val result = barcodeScanner.process(image)
                .addOnSuccessListener {
                    qr = it.get(0)
                }
                .addOnFailureListener{
                    Log.e("QRScanner", "scan failed", it)
                }
                .addOnCompleteListener{
                    imageProxy.close()
                }
    }


    fun toEventSpec() : EventSpec {
        // Handle the dates
        //val startMonthDayYear: IntArray = intArrayOf(qr.start.month, qr.start.day, qr.start.year)
        //val endMonthDayYear: IntArray =  intArrayOf(qr.end.month, qr.end.day, qr.end.year)

        // Handle the hours and minutes
        //val startHourMinutes: IntArray =  intArrayOf(qr.start.hours, qr.start.minutes)
        //val endHourMinutes: IntArray = intArrayOf(qr.end.hours, qr.end.minutes)
        val qrEvent = qr.calendarEvent
        val event = EventSpec.Builder(0)
                .title(qrEvent.summary)
                .description(qrEvent.description)
                .location(qrEvent.location)
                .start(toZonedDateTime(qrEvent.start.month, qrEvent.start.day, qrEvent.start.year, qrEvent.start.hours, qrEvent.start.minutes))
                .end(toZonedDateTime(qrEvent.end.month, qrEvent.end.day, qrEvent.end.year, qrEvent.end.hours, qrEvent.end.minutes))
                .build()
        return event
    }

    fun qrResult(): Observable<Barcode> {
        return  Observable.just(qr.calendarEvent)
    }
}