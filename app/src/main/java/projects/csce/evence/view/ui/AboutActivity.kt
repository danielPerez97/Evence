package projects.csce.evence.view.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import projects.csce.evence.R
import projects.csce.evence.databinding.ActivityAboutBinding


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