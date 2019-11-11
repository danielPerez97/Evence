@file:JvmName("Utils")

package projects.csce.evence.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import projects.csce.evence.BaseApplication
import projects.csce.evence.di.AppComponent

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
