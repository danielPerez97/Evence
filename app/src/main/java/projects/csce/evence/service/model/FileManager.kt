package projects.csce.evence.service.model

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import okio.buffer
import okio.sink
import projects.csce.evence.ical.ICalSpec
import projects.csce.evence.ical.Parser
import projects.csce.evence.service.model.qr.QrBitmapGenerator
import java.io.File

class FileManager(val context: Context, val qrBitmapGenerator: QrBitmapGenerator)
{
	private val processor = PublishProcessor.create<List<ICalSpec>>()
	private val icalDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "/ical")

	init
	{
		icalDir.mkdirs()
	}


	fun saveICalFile(ical: ICalSpec)
	{
		// Create our file
		Log.e("FILENAMECREATE", ical.fileName)
//		val newFile = File.createTempFile(ical.fileName, ".ics", icalDir)
		val newFile = File("$icalDir/${ical.fileName}.ics")

		// Write the file
		newFile.sink().buffer().use {
			it.writeUtf8(ical.text())
		}

		// Notify of change
		notifyIcals()

		// Save it's image
		saveICalImage(ical)
	}

	private fun saveICalImage(ical: ICalSpec)
	{
		val bitmap: Bitmap = qrBitmapGenerator.forceGenerate(ical.events[0])

		// Create the image file
		val newFile = File("${icalDir}/image_${ical.fileName}.png")

		// Write the file
		newFile.sink().buffer().use {
			bitmap.compress(Bitmap.CompressFormat.PNG, 85, it.outputStream())
		}
	}

	fun notifyIcals()
	{
		// Notify of change
		val files = icalDir.listFiles()
		val parsedIcals: List<ICalSpec> = files.toList().filter { !it.name.contains("image_") }.map { Parser.parse(it) }
		processor.onNext(parsedIcals)
	}

	fun notifyEvents()
	{
		// Notify of change
		val files = icalDir.listFiles()
		if(files == null)
		{
			Log.i("UPSTREAM", "FOUND NO FILES")
		}
		else
		{
			Log.i("UPSTREAM", "FOUND FILES")
			Log.i("UPSTREAM", "${files.size}")
		}
		val parsedIcals: List<ICalSpec> = files.toList().map { Parser.parse(it) }
		val events = parsedIcals.flatMap { it.events }
//		processor.onNext(parsedIcals.flatMap { it.events })
	}

	fun getFileUri(fileName: String): Uri?
	{
		Log.e("FILENAME", fileName)
		Log.e("FILENAME", icalDir.listFiles()[1].toString())
		val file: File = icalDir.listFiles().find { it.name.contains(fileName) }!!
		return FileProvider.getUriForFile(context, "projects.csce.evence.FileProvider", file)
	}

	fun getFilePath(fileName: String): String
	{
		val file: File? = icalDir.listFiles().find { it.name.contains(fileName) }
		return file!!.absolutePath
	}

	fun icals(): Flowable<List<ICalSpec>>
	{
		return processor
	}
}