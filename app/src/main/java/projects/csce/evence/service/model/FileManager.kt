package projects.csce.evence.service.model

import android.content.Context
import android.os.Environment
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import okio.buffer
import okio.sink
import projects.csce.evence.ical.ICalSpec
import projects.csce.evence.ical.Parser
import java.io.File

class FileManager(context: Context)
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
		val newFile = File.createTempFile(ical.fileName, ".ics", icalDir)

		// Write the file
		newFile.sink().buffer().use {
			it.writeUtf8(ical.text())
		}

		// Notify of change
		notifyIcals()
	}

	fun notifyIcals()
	{
		// Notify of change
		val files = icalDir.listFiles()
		val parsedIcals: List<ICalSpec> = files.toList().map { Parser.parse(it) }
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