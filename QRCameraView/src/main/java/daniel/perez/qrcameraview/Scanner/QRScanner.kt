package daniel.perez.qrcameraview.Scanner

import android.util.Log
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class QRScanner() : BaseAnalyzer()
{
    private val barcodeSubject: PublishSubject<List<Barcode>> = PublishSubject.create()
    private lateinit var barcodeScanner : BarcodeScanner


    override fun scan(){
        barcodeScanner = BarcodeScanning.getClient()
        val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_AZTEC,
                        Barcode.FORMAT_UPC_A,
                        Barcode.FORMAT_UPC_E)
                .build()
        val result = barcodeScanner.process(inputImage)
                .addOnSuccessListener {
                    barcodeSubject.onNext(it)
                    if (it.size !=0) {
                        Log.d("QrHandler", "scan successful" + it[0].rawValue.toString())
                    }
                    Timber.i("PROXYYYY width=========" + imageProxy.width + "height==========" +  imageProxy.height )

                }
                .addOnFailureListener{
                    Log.e("QRScanner", "scan failed", it)
                }
                .addOnCompleteListener{
                    imageProxy.close()
                }
    }

    fun qrScannerResult(): Observable<List<Barcode>> = barcodeSubject //switch to using List

    override fun close() = barcodeScanner.close()

}