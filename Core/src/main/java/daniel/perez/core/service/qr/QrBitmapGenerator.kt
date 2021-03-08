package daniel.perez.core.service.qr

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

class QrBitmapGenerator(private val writer: MultiFormatWriter, private val encoder: BarcodeEncoder)
{
	fun generate(iCalText: String): Bitmap
	{
		val bitMatrix = writer.encode(iCalText, BarcodeFormat.QR_CODE, 600, 600)
		return encoder.createBitmap(bitMatrix)
	}
}