package projects.evenceteam.evence.view.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import dagger.hilt.android.AndroidEntryPoint
import teamevence.evenceapp.core.ActivityStarter
import projects.evenceteam.evence.R
import projects.evenceteam.evence.databinding.ActivityAboutBinding
import javax.inject.Inject


@AndroidEntryPoint
class AboutActivity : AppCompatActivity() {

    lateinit var binding: ActivityAboutBinding
    @Inject
    lateinit var navigator: ActivityStarter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.emailButton.setOnClickListener { sendFeedback() }
        binding.privacyPolicyContentText.text = HtmlCompat.fromHtml(
            getString(R.string.privacy_policy_content),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    fun sendFeedback() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            val email = getString(R.string.evence_email)
            data = Uri.parse("mailto:")
            intent.type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.evence_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.evence_feedback))
        }

        try {
        startActivity(Intent.createChooser(intent, "Select your Email app"))
        }
        catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun leaveRating() {
        //todo: open link to playstore page here
    }

    fun privacyPolicy(view: View) {
        if (binding.privacyPolicyContentText.visibility == View.GONE) {
            binding.privacyPolicyContentText.visibility = View.VISIBLE
            binding.privacyPolicyText.visibility = View.GONE
        } else {
            binding.privacyPolicyContentText.visibility = View.GONE
            binding.privacyPolicyText.visibility = View.VISIBLE
        }
    }

    fun licenses(view: View) {
        navigator.startLicenseActivity(this)
    }

}