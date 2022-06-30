package projects.evenceteam.evence.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import projects.evenceteam.evence.databinding.ActivityLoggedInBinding

class SecondActivity : AppCompatActivity()
{
    private var binding: ActivityLoggedInBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoggedInBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }
}