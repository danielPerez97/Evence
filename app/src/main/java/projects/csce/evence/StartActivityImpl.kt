package projects.csce.evence

import android.content.Context
import android.content.Intent
import daniel.perez.core.StartActivity
import daniel.perez.core.model.ViewCalendarData
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.service.FileManager
import daniel.perez.generateqrview.GenerateQR
import daniel.perez.qrcameraview.QrReaderActivity
import projects.csce.evence.view.ui.AboutActivity
import projects.csce.evence.view.ui.SecondActivity
import projects.csce.evence.view.ui.SettingsActivity
import projects.csce.evence.view.ui.ShareAppActivity
import javax.inject.Inject

class StartActivityImpl @Inject constructor(private val fileManager: FileManager): StartActivity
{
    override fun startGenerateQr(activity: Context)
    {
        val generateQRActivity = Intent(activity, GenerateQR::class.java)
        activity.startActivity(generateQRActivity)
    }

    override fun startEditQr(activity: Context, ical: ViewEvent)
    {
        val intent = Intent(activity, GenerateQR::class.java)
//        intent.putExtra("FILE_PATH", fileManager.getFilePath(ical.fileName))
        activity.startActivity(intent)
    }

    override fun startQrReader(activity: Context)
    {
        val qrReaderActivity = Intent(activity, QrReaderActivity::class.java)
        activity.startActivity(qrReaderActivity)
    }

    override fun startSecondActivity(activity: Context)
    {
        val secondActivityIntent = Intent(activity, SecondActivity::class.java)
        activity.startActivity(secondActivityIntent)
    }

    override fun startSettingsActivity(activity: Context)
    {
        val settingsActivity = Intent(activity, SettingsActivity::class.java)
        activity.startActivity(settingsActivity)
    }

    override fun startShareAppActivity(activity: Context)
    {
        val shareActivity = Intent(activity, ShareAppActivity::class.java)
        activity.startActivity(shareActivity)
    }

    override fun startAboutActivity(activity: Context)
    {
        val aboutActivity = Intent(activity, AboutActivity::class.java)
        activity.startActivity(aboutActivity)
    }
}