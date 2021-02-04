package daniel.perez.qrcameraview

import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class TextScanner() : BaseAnalyzer() {
    private val scannedTextSubject: PublishSubject<Text> = PublishSubject.create()
    private val textRecognizer = TextRecognition.getClient()
    private lateinit var recognizer: TextRecognizer


    fun textScannerResult(): Observable<Text> {
        return scannedTextSubject
    }

    override fun initialize(){
        recognizer = TextRecognition.getClient()
    }

    override fun scan() {
        Timber.i("Scanning text")
        val result = textRecognizer.process(inputImage)
                .addOnSuccessListener {
                    val text = it.text
                    Timber.i("Scanned Text: " + text)
                    for (block in it.textBlocks) {
                        val blockText = block.text
                        Timber.i("Scanned blockText: " + blockText)
                        for (line in block.lines) {
                            val lineText = line.text
                            Timber.i("Scanned LineText: " + lineText)
                            for (element in line.elements) {
                                val elementText = element.text
                                Timber.i("Scanned elementText: " + elementText)
                            }
                        }
                    }

                }
                .addOnFailureListener {
                    Timber.i("Scanned text failed")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
    }

    override fun close() {
        recognizer.close()
    }
}