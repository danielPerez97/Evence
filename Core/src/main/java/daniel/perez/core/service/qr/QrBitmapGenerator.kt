package daniel.perez.core.service.qr

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import daniel.perez.ical.EventSpec
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.PublishProcessor

class QrBitmapGenerator(private val writer: MultiFormatWriter, private val encoder: BarcodeEncoder)
{
	private val attempts = PublishProcessor.create<QrAttempt>()

	fun generate(qr: EventSpec)
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

	fun forceGenerate(iCalText: String): Bitmap
	{
		val bitMatrix = writer.encode(iCalText, BarcodeFormat.QR_CODE, 600, 600)
		return encoder.createBitmap(bitMatrix)
	}

	fun generations(): Flowable<QrAttempt> = attempts

}