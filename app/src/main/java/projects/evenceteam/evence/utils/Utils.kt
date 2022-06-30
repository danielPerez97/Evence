@file:JvmName("Utils")

package projects.evenceteam.evence.utils

import android.content.Context
import android.widget.Toast
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