package daniel.perez.core.service

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import dagger.hilt.android.qualifiers.ApplicationContext
import daniel.perez.core.BaseActivity
import daniel.perez.core.db.Event
import daniel.perez.core.service.qr.QrBitmapGenerator
import daniel.perez.ical.ICalSpec
import okio.buffer
import okio.sink
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class FileManager @Inject constructor(
		@ApplicationContext private val context: Context,
		private val qrBitmapGenerator: QrBitmapGenerator
)
{
	private val icalDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "/ical")

	init
	{
		icalDir.mkdirs()
	}

	fun getOrSaveImage(ical: ICalSpec, fileName: String): File
	{
		return saveImage(ical, fileName, false)
	}

	fun updateImage(ical: ICalSpec, uri: Uri)
	{
		val fileName: String = uri.toFile().name
		saveImage(ical, fileName, true)
	}

	fun getOrSaveIcs(ical: ICalSpec, fileName: String): File
	{
		return saveIcs(ical, fileName, false)
	}

	fun updateIcs(ical: ICalSpec, uri: Uri)
	{
		val fileName: String = uri.toFile().name
		saveIcs(ical, fileName, true)
	}

	private fun saveIcs(ical: ICalSpec, fileName: String, forceOverwrite: Boolean ): File
	{
		// Create the ics file
		val icsFile = File("$icalDir/$fileName.ics")

		// Check if the ics file has already been made
		if(icsFile.exists() && !forceOverwrite)
		{
			return icsFile
		}

		// Create the ics data to write
		val icsString = ical.text()

		// Write the file
		icsFile.sink().buffer().use {
			it.writeUtf8( icsString )
		}

		return icsFile
	}


	private fun saveImage(ical: ICalSpec, fileName: String, forceOverwrite: Boolean): File
	{
		// Create the image file
		val imageFile = File("${icalDir}/image_$fileName.png")

		// Check if the image has already been made
		if(imageFile.exists() && !forceOverwrite)
		{
			return imageFile
		}

		// Create the bitmap data to write
		val bitmap: Bitmap = qrBitmapGenerator.generate(ical.text())

		// Write the file
		imageFile.sink().buffer().use {
			bitmap.compress(Bitmap.CompressFormat.PNG, 85, it.outputStream())
		}

		return imageFile
	}

	fun getContentUri(fileName: String): Uri?
	{
		val file: File = icalDir.listFiles().find { it.name.contains(fileName) }!!
		return FileProvider.getUriForFile(context, "${context.applicationContext.packageName}.fileprovider", file)
	}

	fun writeFileActionCreateDocument(context: BaseActivity, event: Event, data: Intent)
	{
		val uri: Uri? = data.data
		try
		{
			val pfd = context.contentResolver.openFileDescriptor(uri!!, "w")
			val fileOutputStream = FileOutputStream(pfd!!.fileDescriptor)
			try
			{
				fileOutputStream.sink().buffer().use { sink -> sink.writeUtf8( event.icsText() ) }
			}
			catch (e: IOException)
			{
				Timber.i(e)
				e.printStackTrace()
			}
			pfd.close()
		} catch (e: FileNotFoundException)
		{
			Timber.i(e)
			e.printStackTrace()
		} catch (e: IOException)
		{
			e.printStackTrace()
		}
		Timber.i("Wrote File")
	}

}