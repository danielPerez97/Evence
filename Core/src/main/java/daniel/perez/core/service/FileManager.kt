package daniel.perez.core.service

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import daniel.perez.core.service.qr.QrBitmapGenerator
import okio.buffer
import okio.sink
import daniel.perez.ical.ICalSpec
import daniel.perez.ical.Parser
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.io.File

class FileManager(val context: Context, val qrBitmapGenerator: QrBitmapGenerator)
{
	private val processor = PublishSubject.create<List<ICalSpec>>()
	private val icalDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "/ical")

	init
	{
		icalDir.mkdirs()
	}


	fun saveICalFile(ical: ICalSpec)
	{
		// Create our file
		Timber.e(ical.fileName)
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
		val bitmap: Bitmap = qrBitmapGenerator.forceGenerate(ical.text())

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
		processor.onNext( getCurrentFiles() )
	}

	private fun getCurrentFiles(): List<ICalSpec>
	{
		val files: Array<File>? = icalDir.listFiles()
		if(files == null)
		{
			Timber.i("FOUND NO FILES")
		}
		else
		{
			Timber.i("FOUND FILES")
			Timber.tag("UPSTREAM").i(files.size.toString())
		}
		return files?.toList()?.map { Parser.parse(it) } ?: emptyList()
	}

	fun getFileUri(fileName: String): Uri?
	{
		Timber.tag("FILENAME").e(fileName)
		Timber.tag("FILENAME").e(icalDir.listFiles()[1].toString())
		val file: File = icalDir.listFiles().find { it.name.contains(fileName) }!!
		return FileProvider.getUriForFile(context, "projects.csce.evence.FileProvider", file)
	}

	fun getFilePath(fileName: String): String
	{
		val file: File? = icalDir.listFiles().find { it.name.contains(fileName) }
		return file!!.absolutePath
	}

	fun icals(): Observable<List<ICalSpec>>
	{
		val currentIcals: Observable<List<ICalSpec>> = Observable.just( getCurrentFiles() )
		return Observable.merge(processor, currentIcals)
				.doOnNext{ Timber.i( "Size of array: ${it.size}" ) }
	}
}