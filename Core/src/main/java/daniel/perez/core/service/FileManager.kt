package daniel.perez.core.service

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import daniel.perez.core.service.qr.QrBitmapGenerator
import daniel.perez.ical.ICalSpec
import okio.buffer
import okio.sink
import timber.log.Timber
import java.io.File

class FileManager(val context: Context, private val qrBitmapGenerator: QrBitmapGenerator)
{
	private val icalDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "/ical")

	init
	{
		icalDir.mkdirs()
	}

	fun getOrSaveImage(ical: ICalSpec, fileName: String): Uri
	{
		Timber.i("Saving image $fileName")
		val uri = saveImage(ical, fileName, false)
		Timber.i("$fileName URI: $uri")
		return uri
	}

	fun updateImage(ical: ICalSpec, uri: Uri)
	{
		val fileName: String = uri.toFile().name
		Timber.i("Updating File: $fileName")
		saveImage(ical, fileName, true)
	}

	fun getOrSaveIcs(ical: ICalSpec, fileName: String): Uri
	{
		Timber.i("Saving ics $fileName")
		return saveIcs(ical, fileName, false)
	}

	fun updateIcs(ical: ICalSpec, uri: Uri)
	{
		val fileName: String = uri.toFile().name
		Timber.i("Updating File: $fileName")
		saveIcs(ical, fileName, true)
	}

	private fun saveIcs(ical: ICalSpec, fileName: String, forceOverwrite: Boolean ): Uri
	{
		// Create the ics file
		val icsFile = File("$icalDir/$fileName.ics")

		// Check if the ics file has already been made
		if(icsFile.exists() && !forceOverwrite)
		{
			return Uri.fromFile(icsFile)
		}

		// Create the ics data to write
		val icsString = ical.text()

		// Write the file
		icsFile.sink().buffer().use {
			it.writeUtf8( icsString )
		}

		return Uri.fromFile(icsFile)
	}


	private fun saveImage(ical: ICalSpec, fileName: String, forceOverwrite: Boolean): Uri
	{
		// Create the image file
		val imageFile = File("${icalDir}/image_$fileName.png")

		// Check if the image has already been made
		if(imageFile.exists() && !forceOverwrite)
		{
			return Uri.fromFile(imageFile)
		}

		// Create the bitmap data to write
		val bitmap: Bitmap = qrBitmapGenerator.generate(ical.text())

		// Write the file
		imageFile.sink().buffer().use {
			bitmap.compress(Bitmap.CompressFormat.PNG, 85, it.outputStream())
		}

		return Uri.fromFile(imageFile)
	}

	private fun getFileUri(fileName: String): Uri?
	{
		Timber.tag("FILENAME").e(fileName)
		Timber.tag("FILENAME").e(icalDir.listFiles()[1].toString())
		val file: File = icalDir.listFiles().find { it.name.contains(fileName) }!!
		return FileProvider.getUriForFile(context, "projects.csce.evence.FileProvider", file)
	}

}