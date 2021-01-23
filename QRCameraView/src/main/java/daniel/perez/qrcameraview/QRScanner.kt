package daniel.perez.qrcameraview

import android.annotation.SuppressLint
import android.util.Log
//import androidx.camera.core.ImageAnalysis
//import androidx.camera.core.ImageProxy
//import com.google.android.gms.tasks.Task
//import com.google.mlkit.vision.barcode.Barcode
//import com.google.mlkit.vision.barcode.BarcodeScannerOptions
//import com.google.mlkit.vision.barcode.BarcodeScanning
//import com.google.mlkit.vision.common.InputImage

class QRScanner //: ImageAnalysis.Analyzer {
//    @SuppressLint("UnsafeExperimentalUsageError")
//    override fun analyze(imageProxy: ImageProxy) {
//        val options = BarcodeScannerOptions.Builder()
//                .setBarcodeFormats(
//                        Barcode.FORMAT_QR_CODE,
//                        Barcode.FORMAT_AZTEC,
//                        Barcode.FORMAT_UPC_A,
//                        Barcode.FORMAT_UPC_E
//                )
//                .build()
//        val barcodeScanner = BarcodeScanning.getClient()
//        val mediaImage = imageProxy.image
//        if (mediaImage != null) {
//            val image = InputImage.fromMediaImage(mediaImage, 0)
//            val result = barcodeScanner.process(image)
//                    .addOnSuccessListener { barcodes ->
//                        for(barcode in barcodes) {
//                            val rawValue = barcode.rawValue
//                            Log.d("QRScanner", "raw vavlue=$rawValue" )
//                        }
//                    }
//                    .addOnFailureListener{
//                        Log.e("QRScanner", "scan failed", it)
//                    }
//        }
//    }
//}