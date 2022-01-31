package projects.csce.evence.view.ui

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.fromHtml
import dagger.hilt.android.AndroidEntryPoint
import daniel.perez.core.ActivityStarter
import projects.csce.evence.R
import projects.csce.evence.databinding.ActivityAboutBinding
import javax.inject.Inject


@AndroidEntryPoint
class AboutActivity : AppCompatActivity()
{

    lateinit var binding: ActivityAboutBinding
    @Inject lateinit var navigator: ActivityStarter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cardView.setOnClickListener { navigator.startLicenseActivity(this) }
        binding.privacyPolicyContentText.text =  HtmlCompat.fromHtml(getString(R.string.privacy_policy_content), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    fun sendFeedback()
    {
        //todo: which email to use?
    }

    fun leaveRating()
    {
        //todo: open link to playstore page here
    }

    fun privacyPolicy(view: View){
        if(binding.privacyPolicyContentText.visibility == View.GONE) {
            binding.privacyPolicyContentText.visibility = View.VISIBLE
            binding.privacyPolicyText.visibility = View.GONE
        }
        else {
            binding.privacyPolicyContentText.visibility = View.GONE
            binding.privacyPolicyText.visibility = View.VISIBLE
        }
    }

}