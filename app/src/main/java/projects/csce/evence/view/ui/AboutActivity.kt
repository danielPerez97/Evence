package projects.csce.evence.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import daniel.perez.core.ActivityStarter
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

    }

    fun sendFeedback()
    {
        //todo: which email to use?
    }

    fun leaveRating()
    {
        //todo: open link to playstore page here
    }

    fun shhhh()
    {

    }
}