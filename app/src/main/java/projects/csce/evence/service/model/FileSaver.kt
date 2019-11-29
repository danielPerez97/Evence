package projects.csce.evence.service.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import projects.csce.evence.service.model.event.Event
import java.io.File
import javax.inject.Inject

class FileSaver @Inject constructor(private val context: Context)
{
	private val bitmaps = PublishProcessor.create<Bitmap>()

	fun saveBitmap(event: Event, bitmap: Bitmap)
	{
		// Save it into file storage
		context.openFileOutput("${event.title}.png", Context.MODE_PRIVATE).use {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
		}

		// Propagate the change
		val bm = File(context.filesDir, "${event.title}.png")

		bitmaps.onNext(bm.toBitmap())
	}

	fun bitmaps(): Flowable<Bitmap> = bitmaps

	private fun File.toBitmap(): Bitmap
	{
		return BitmapFactory.decodeFile(this.path)
	}
}