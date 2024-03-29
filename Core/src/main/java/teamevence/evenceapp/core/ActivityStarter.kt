package teamevence.evenceapp.core

import android.content.Context
import teamevence.evenceapp.core.model.ViewEvent

interface ActivityStarter
{
    fun startGenerateQr(activity: Context)

    fun startNewEventActivity(activity: Context)

    fun startEditQr(activity: Context, ical: ViewEvent)

    fun startQrReader(activity: Context)

    fun startSecondActivity(activity: Context)

    fun startSettingsActivity(activity: Context)

    fun startShareAppActivity(activity: Context)

    fun startAboutActivity(activity: Context)

    fun startLicenseActivity(activity: Context)
}