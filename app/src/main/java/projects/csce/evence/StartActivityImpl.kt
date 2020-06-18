package projects.csce.evence

import android.content.Context
import android.content.Intent
import daniel.perez.core.StartActivity
import daniel.perez.generateqrview.GenerateQR
import javax.inject.Inject

class StartActivityImpl @Inject constructor(): StartActivity
{
    override fun startGenerateQr(activity: Context, filePath: String)
    {
        val intent = Intent(activity, GenerateQR::class.java)
        intent.putExtra("FILE_PATH", filePath);
        activity.startActivity(intent);
    }
}