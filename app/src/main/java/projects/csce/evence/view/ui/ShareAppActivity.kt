package projects.csce.evence.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import projects.csce.evence.databinding.ActivityShareAppBinding

class ShareAppActivity : AppCompatActivity() {
    lateinit var binding: ActivityShareAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun shareAppQr() {
        //todo: share qr with playstore link
    }

    fun shareApp() {
        //todo: share playstore link directly
    }
}
