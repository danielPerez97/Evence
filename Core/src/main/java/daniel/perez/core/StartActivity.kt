package daniel.perez.core

import android.content.Context
import daniel.perez.core.model.ViewCalendarData

interface StartActivity
{
    fun startGenerateQr(activity: Context)

    fun startEditQr(activity: Context, ical: ViewCalendarData)

    fun startQrReader(activity: Context)

    fun startSecondActivity(activity: Context)

    fun startSettingsActivity(activity: Context)

    fun startShareAppActivity(activity: Context)

    fun startAboutActivity(activity: Context)
}