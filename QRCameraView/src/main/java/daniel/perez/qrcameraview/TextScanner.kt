package daniel.perez.qrcameraview

import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class TextScanner() : BaseAnalyzer() {
    private val scannedTextSubject: PublishSubject<Text> = PublishSubject.create()
    private val textRecognizer = TextRecognition.getClient()

    fun textScannerResult(): Observable<Text> {
        return scannedTextSubject
    }

    override fun scan() {
        val result = textRecognizer.process(inputImage)
                .addOnSuccessListener {
                    scannedTextSubject.onNext(it)
                    Timber.i("Scanned Text: " + it.text)
                }
                .addOnFailureListener {
                    Timber.i("Scanned text failed")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
    }
}