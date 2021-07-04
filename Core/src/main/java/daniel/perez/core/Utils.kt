@file:JvmName("Utils")
package daniel.perez.core

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import daniel.perez.core.model.DateSetEvent
import daniel.perez.core.model.Half
import daniel.perez.core.model.TimeSetEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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

fun ZonedDateTime.dateString(): String
{
    return this.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
}

fun ZonedDateTime.timeString(): String
{
    return this.format(DateTimeFormatter.ofPattern("hh:mm a"))
}

//auto set date format according to devices locale
fun formatLocaleDate(dateString: String): String {
    val initialDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US).parse(dateString)
    val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Resources.getSystem().configuration.locales.get(0)
    } else {
        Resources.getSystem().configuration.locale
    }

    val newDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale)
    Timber.i("Before: $dateString")
    Timber.i("After ${newDateFormat.format(initialDateFormat)}")
    return newDateFormat.format(initialDateFormat)
}

fun getLocaleMonth(dateString: String) : String {
    val initialDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US).parse(dateString)
    val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Resources.getSystem().configuration.locales.get(0)
    } else {
        Resources.getSystem().configuration.locale
    }
    val dateFormat = SimpleDateFormat("LLLL", locale)
    return dateFormat.format(initialDateFormat)
}

fun getDay(dateString: String) : String
{
    val initialDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US).parse(dateString)
    val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
    return dateFormat.format(initialDateFormat)
}

fun getYear(dateString: String) : String
{
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

fun toLocalDateTime(day: Int, month: Int, year: Int, hour: Int, minute: Int) : LocalDateTime {
    val date = DateSetEvent(toOneIfNeg(month),toOneIfNeg(day), to1111IfNeg(year))
    val time = TimeSetEvent(toZeroIfNeg(hour), toZeroIfNeg(minute), Half.AM)

    return LocalDateTime.parse("${date.string()}T${time.string()}")
}

fun LocalDateTime.toAMPM(): String
{
    return this.format(DateTimeFormatter.ofPattern("hh:mm a"))
}

fun toZeroIfNeg(num : Int) : Int{
    return if(num < 0) 0 else num
}

fun toOneIfNeg(num : Int) : Int{
    return if(num < 0) 1 else num
}

fun to1111IfNeg(num: Int): Int{
    return if(num < 0) 1111 else num
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

operator fun CompositeDisposable.plusAssign(disposable: Disposable)
{
    this.add(disposable)
}

fun copyToClipboard(context: Context, label: String, copiedString: String) {
    val clipboard = context.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, copiedString)
    clipboard.setPrimaryClip(clip)
    context.toastShort("Text copied")
}

fun getScreenWidthPx(context : Context ) : Int{
    return context.resources.displayMetrics.widthPixels
}

fun getScreenHeightPx(context : Context ) : Int{
    return  context.resources.displayMetrics.heightPixels
}

fun convertDPtoPX(context : Context, dp : Float) : Int {
        return (dp * (context.resources.displayMetrics.densityDpi / 160f)).toInt()
}