package daniel.perez.core

import android.content.Context

interface StartActivity
{
    fun startGenerateQr(activity: Context, filePath: String)
}