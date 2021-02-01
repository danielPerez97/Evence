package daniel.perez.qrcameraview

import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class QrHandler() {
    private val barcodeSubject: PublishSubject<MutableList<Barcode>> = PublishSubject.create()

    fun scanBarcode(imageProxy: ImageProxy, image : InputImage){
        //Gets instance of BarcodeScanner. where the magic happens
        val barcodeScanner = BarcodeScanning.getClient()
        val result = barcodeScanner.process(image)
                .addOnSuccessListener {
                    barcodeSubject.onNext(it)
                    if (it.size !=0)
                        Log.d("QrHandler", "scan successful" + it[0].rawValue.toString())
                }
                .addOnFailureListener{
                    Log.e("QRScanner", "scan failed", it)
                }
                .addOnCompleteListener{
                    imageProxy.close()
                }
    }

    fun qrResult(): Observable<MutableList<Barcode>> {
        return barcodeSubject
    }
}