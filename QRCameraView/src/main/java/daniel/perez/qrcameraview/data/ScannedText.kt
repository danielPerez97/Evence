package daniel.perez.qrcameraview.data

import com.google.mlkit.vision.text.Text

class ScannedText(type: SCAN_TYPE, val data: Text.TextBlock) : ScannedData(type) {
}