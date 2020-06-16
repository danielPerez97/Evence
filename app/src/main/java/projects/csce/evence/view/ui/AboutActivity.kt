package projects.csce.evence.view.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import projects.csce.evence.R
import projects.csce.evence.databinding.ActivityAboutBinding
import projects.csce.evence.databinding.ActivitySettingsBinding
import projects.csce.evence.service.model.SharedPref
import projects.csce.evence.utils.getAppComponent
import javax.inject.Inject


class AboutActivity : AppCompatActivity() {
    lateinit var binding: ActivityAboutBinding
    var count : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about)
        binding.view = this
        binding.lifecycleOwner = this
    }

    fun sendFeedback(){
        //todo: which email to use?
    }

    fun leaveRating(){
        //todo: open link to playstore page here
    }

    fun shhhh(){
        count++

        if(count == 8)
            Toast.makeText(this, "Yay! you found it", Toast.LENGTH_SHORT).show()
        if(count == 18)
            Toast.makeText(this, "Leave a rating on the Playstore", Toast.LENGTH_SHORT).show()
        if(count == 30)
            Toast.makeText(this, "So... um how ya doing?", Toast.LENGTH_SHORT).show()
        if(count >= 40)
            Toast.makeText(this, "You can stop now", Toast.LENGTH_SHORT).show()
    }
}