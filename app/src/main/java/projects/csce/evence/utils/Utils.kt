@file:JvmName("Utils")

package projects.csce.evence.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.component.VEvent
import org.threeten.bp.ZonedDateTime
import projects.csce.evence.BaseApplication
import projects.csce.evence.di.appscope.AppComponent

fun AppCompatActivity.getAppComponent(): AppComponent
{
	return (this.application as BaseApplication).injector
}

fun Context.toastShort(message: String)
{
	Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(message: String)
{
	Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Array<String>.toInts(): IntArray
{
	return this.map { it.toInt() }.toIntArray()
}

fun IntArray.toZonedDateTime(): ZonedDateTime
{
	return ZonedDateTime.now().withMonth(this[0]).withDayOfMonth(this[1]).withYear(this[2])
}