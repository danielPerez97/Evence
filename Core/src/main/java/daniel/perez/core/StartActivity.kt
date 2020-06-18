package daniel.perez.core

import android.content.Context

interface StartActivity
{
    fun startGenerateQr(activity: Context)

    fun startEditQr(activity: Context, filePath: String)

    fun startQrReader(activity: Context)

    fun startSecondActivity(activity: Context)

    fun startSettingsActivity(activity: Context)

    fun startShareAppActivity(activity: Context)

    fun startAboutActivity(activity: Context)
}