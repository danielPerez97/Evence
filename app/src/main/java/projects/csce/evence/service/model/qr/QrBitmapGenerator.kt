package projects.csce.evence.service.model.qr

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import projects.csce.evence.service.model.event.Event

class QrBitmapGenerator(private val writer: MultiFormatWriter, private val encoder: BarcodeEncoder)
{
	private val attempts = PublishProcessor.create<QrAttempt>()
	fun generate(qr: Event)
	{
		try
		{
			val bitMatrix = writer.encode(qr.text(), BarcodeFormat.QR_CODE, 600, 600)
			val bitmap = encoder.createBitmap(bitMatrix)
			attempts.onNext(QrAttempt.Success(bitmap))
		}
		catch (e: Exception)
		{
			attempts.onNext(QrAttempt.Failure(e))
			e.printStackTrace()
		}
	}

	fun generations(): Flowable<QrAttempt> = attempts

}