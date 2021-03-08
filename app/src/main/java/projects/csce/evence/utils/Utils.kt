@file:JvmName("Utils")

package projects.csce.evence.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import projects.csce.evence.BaseApplication
import projects.csce.evence.di.appscope.AppComponent
import java.time.ZonedDateTime

fun Context.toastLong(message: String)
{
	Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun toZonedDateTime(dates: IntArray, hourMinute: IntArray): ZonedDateTime
{
	return ZonedDateTime.now()
			.withHour(hourMinute[0])
			.withMinute(hourMinute[1])
			.withMonth(dates[0])
			.withDayOfMonth(dates[1])
			.withYear(dates[2])
}