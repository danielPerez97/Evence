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

fun toDayMonthYear(date: String) = SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(date)

fun Array<String>.toInts(): IntArray
{
    return this.map { it.toInt() }.toIntArray()
}

fun Context.toastShort(message: String)
{
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}