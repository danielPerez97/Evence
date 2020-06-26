@file:JvmName("Utils")
package daniel.perez.core

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.widget.Toast
import java.text.DateFormat
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

//auto set date format according to devices locale
fun setLocaleDateFormat(dateString: String): String {
    val initialDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US).parse(dateString)
    val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Resources.getSystem().configuration.locales.get(0);
    } else {
        Resources.getSystem().configuration.locale;
    }

    val newDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale)
    return newDateFormat.format(initialDateFormat)
}

fun getLocaleMonth(dateString: String) : String {
    val initialDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US).parse(dateString)
    val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Resources.getSystem().configuration.locales.get(0);
    } else {
        Resources.getSystem().configuration.locale;
    }
    val dateFormat = SimpleDateFormat("LLLL", locale)
    return dateFormat.format(initialDateFormat)
}

fun getDay(dateString: String) : String{
    val initialDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US).parse(dateString)
    val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
    return dateFormat.format(initialDateFormat)
}

fun getYear(dateString: String) : String{
    val initialDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US).parse(dateString)
    val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    return dateFormat.format(initialDateFormat)
}

fun Array<String>.toInts(): IntArray
{
    return this.map { it.toInt() }.toIntArray()
}

fun Context.toastShort(message: String)
{
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}