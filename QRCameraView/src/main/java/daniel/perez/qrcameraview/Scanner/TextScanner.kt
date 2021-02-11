package daniel.perez.qrcameraview.Scanner

import android.graphics.Rect
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class TextScanner() : BaseAnalyzer() {
    private val scannedTextSubject: PublishSubject<Text> = PublishSubject.create()
    private val scannedTextBlockSubject: PublishSubject<List<Text.TextBlock>> = PublishSubject.create()
    private val boundingBoxesSubject: PublishSubject<List<Rect?>> = PublishSubject.create()
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
                    boundingBoxesSubject.onNext(getTextBoundingBoxes(it.textBlocks))
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

    fun getTextBoundingBoxes(textBlocks: List<Text.TextBlock>) : List<Rect?> {
        val boundingBoxes : MutableList<Rect?> = mutableListOf()
        for (block in textBlocks) {
            Timber.i("left======" + (block.boundingBox?.left ?: "NULLLLLLL"))
            boundingBoxes.add(block.boundingBox)

        }
        return boundingBoxes
    }

    fun textScannerResult(): Observable<Text> = scannedTextSubject
    fun textBlockResult(): Observable<List<Text.TextBlock>> = scannedTextBlockSubject
    fun textBoundingBoxes(): Observable<List<Rect?>>  = boundingBoxesSubject

    override fun close() = recognizer.close()
}