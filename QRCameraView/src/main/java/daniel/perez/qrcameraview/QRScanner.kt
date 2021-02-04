package daniel.perez.qrcameraview

import android.util.Log
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class QRScanner() : BaseAnalyzer()
{
    private val barcodeSubject: PublishSubject<MutableList<Barcode>> = PublishSubject.create()
    //private val barcodeScanner : BarcodeScanner = BarcodeScanning.getClient()
    private lateinit var barcodeScanner : BarcodeScanner

     override  fun initialize() {
        barcodeScanner = BarcodeScanning.getClient()
        val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_AZTEC,
                        Barcode.FORMAT_UPC_A,
                        Barcode.FORMAT_UPC_E)
                .build()
    }

    override fun scan(){
        val result = barcodeScanner.process(inputImage)
                .addOnSuccessListener {
                    barcodeSubject.onNext(it)
                    if (it.size !=0) {
                        Log.d("QrHandler", "scan successful" + it[0].rawValue.toString())
                    }
                }
                .addOnFailureListener{
                    Log.e("QRScanner", "scan failed", it)
                }
                .addOnCompleteListener{
                    imageProxy.close()
                }
    }

    fun qrScannerResult(): Observable<MutableList<Barcode>> {
        return barcodeSubject
    }

    override fun close() {
        barcodeScanner.close()
    }
}