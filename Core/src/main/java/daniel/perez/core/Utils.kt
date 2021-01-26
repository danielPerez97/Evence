@file:JvmName("Utils")
package daniel.perez.core

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.*

fun toZonedDateTime(month: Int, dayOfMonth: Int, year: Int, hour: Int, minute: Int): ZonedDateTime
{
    return ZonedDateTime.now()
            .withHour( hour )
            .withMinute( minute )
            .withMonth( month )
            .withDayOfMonth( dayOfMonth )
            .withYear( year )
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

//turns "Hmm" format into "hh:mm a" format
fun getAMPMTimeFormat(timeString: String) : String {
    val initialTimeFormat = SimpleDateFormat("H mm", Locale.US).parse(timeString)
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return dateFormat.format(initialTimeFormat)
}

fun Array<String>.toInts(): IntArray
{
    return this.map { it.toInt() }.toIntArray()
}

fun Context.toastShort(message: String)
{
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.snackbarShort(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .show()
}