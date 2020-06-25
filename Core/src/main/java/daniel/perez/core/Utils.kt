@file:JvmName("Utils")
package daniel.perez.core

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.*

fun toZonedDateTime(dates: IntArray, hourMinute: IntArray): ZonedDateTime
{
    return ZonedDateTime.now()
            .withHour(hourMinute[0])
            .withMinute(hourMinute[1])
            .withMonth(dates[0])
            .withDayOfMonth(dates[1])
            .withYear(dates[2])
}

fun toDayMonthYear(dateString: String): String {
    val dateFormat1 = SimpleDateFormat("MM-dd-yyyy", Locale.US).parse(dateString)
    val dateFormat2 = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    return dateFormat2.format(dateFormat1)
}

fun Array<String>.toInts(): IntArray
{
    return this.map { it.toInt() }.toIntArray()
}

fun Context.toastShort(message: String)
{
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}