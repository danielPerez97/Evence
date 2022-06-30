package teamevence.evenceapp.qrcameraview.Scanner
/**
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber


///FOR FUTURE UPDATES
class TextScanner() : BaseAnalyzer() {
*/

class TextScanner() {

    /*
    private val scannedTextSubject: PublishSubject<Text> = PublishSubject.create()
    private val scannedTextBlockSubject: PublishSubject<List<Text.TextBlock>> = PublishSubject.create()
    private val textRecognizer = TextRecognition.getClient()
    private lateinit var recognizer: TextRecognizer

    override fun scan() {
        recognizer = TextRecognition.getClient()
        Timber.i("Scanning text")
        val result = textRecognizer.process(inputImage)
                .addOnSuccessListener {
                    val text = it.text
                    scannedTextSubject.onNext(it)
                    scannedTextBlockSubject.onNext(it.textBlocks)
                    //Timber.i("Scanned Text: " + text)
                }
                .addOnFailureListener {
                    Timber.i("Scanned text failed")
                    scannedTextSubject.onError(it)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
    }

    fun textScannerResult(): Observable<Text> = scannedTextSubject
    fun textBlockResult(): Observable<List<Text.TextBlock>> = scannedTextBlockSubject

    override fun close() = recognizer.close()

*/
}