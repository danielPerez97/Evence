package daniel.perez.qrcameraview.data

import com.google.mlkit.vision.barcode.Barcode

class ScannedQR(type: SCAN_TYPE, val data: Barcode) : ScannedData(type) {

}