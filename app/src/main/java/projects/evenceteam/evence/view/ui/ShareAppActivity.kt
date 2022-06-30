package projects.evenceteam.evence.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import projects.evenceteam.evence.databinding.ActivityShareAppBinding

class ShareAppActivity : AppCompatActivity() {
    lateinit var binding: ActivityShareAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.aboutPageCardView.setOnClickListener { shareAppQr() }
    }

    fun shareAppQr() {
        //todo: share qr with playstore link
    }

    fun shareApp() {
        //todo: share playstore link directly
    }
}
