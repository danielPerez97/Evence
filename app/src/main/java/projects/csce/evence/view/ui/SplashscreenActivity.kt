package projects.csce.evence.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashscreenActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)
        finish()
    }
}