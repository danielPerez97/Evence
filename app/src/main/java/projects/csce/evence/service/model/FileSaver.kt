package projects.csce.evence.service.model

import android.content.Context
import android.os.Environment
import okio.buffer
import okio.sink
import projects.csce.evence.ical.ICalSpec
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileSaver @Inject constructor(private val context: Context)
{
	fun saveICalFile(ical: ICalSpec)
	{
		// Get the external storage directory
		val dir = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)
		val icalDir = File(dir, "/ical")
		icalDir.mkdirs()

		// Create our file
		val newFile = File.createTempFile(ical.fileName, ".ics", icalDir)

		// Write the file
		newFile.sink().buffer().use {
			it.writeUtf8(ical.text())
		}
	}
}