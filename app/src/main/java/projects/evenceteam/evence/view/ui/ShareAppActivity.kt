package projects.evenceteam.evence.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import projects.evenceteam.evence.R
import projects.evenceteam.evence.databinding.ActivityShareAppBinding

class ShareAppActivity : AppCompatActivity() {
    lateinit var binding: ActivityShareAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.aboutPageCardView.setOnClickListener { shareAppQr() }
        binding.shareBtn.setOnClickListener { shareAppLink() }
    }

    fun shareAppQr() {
        //do nothing for now
    }

    fun shareAppLink() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this Evence app: " + getString(R.string.evence_playstore_url)
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}
