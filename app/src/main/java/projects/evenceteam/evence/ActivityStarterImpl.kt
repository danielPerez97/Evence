package projects.evenceteam.evence

import android.content.Context
import android.content.Intent
import teamevence.evenceapp.core.ActivityStarter
import teamevence.evenceapp.core.model.ViewEvent
import teamevence.evenceapp.core.service.FileManager
import teamevence.evenceapp.generateqrview.GenerateQR
import teamevence.evenceapp.generateqrview.compose.NewEventActivity
import teamevence.evenceapp.licensesview.LicensesActivity
import teamevence.evenceapp.qrcameraview.ui.QrReaderActivity
import projects.evenceteam.evence.view.ui.AboutActivity
import projects.evenceteam.evence.view.ui.SecondActivity
import projects.evenceteam.evence.view.ui.SettingsActivity
import projects.evenceteam.evence.view.ui.ShareAppActivity
import javax.inject.Inject


class ActivityStarterImpl @Inject constructor(private val fileManager: FileManager): ActivityStarter
{
    override fun startGenerateQr(activity: Context)
    {
        val generateQRActivity = Intent(activity, GenerateQR::class.java)
        activity.startActivity(generateQRActivity)
    }

    override fun startNewEventActivity(activity: Context)
    {
        val intent = Intent(activity, NewEventActivity::class.java)
        activity.startActivity(intent)
    }

    override fun startEditQr(activity: Context, ical: ViewEvent)
    {
        val intent = Intent(activity, GenerateQR::class.java)
        intent.putExtra("EVENT_ID", ical.id)
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

    override fun startLicenseActivity(activity: Context)
    {
        val licenseActivity = Intent(activity, LicensesActivity::class.java)
        activity.startActivity(licenseActivity)
    }
}