package projects.csce.evence.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import projects.csce.evence.databinding.ActivityLoggedInBinding

class SecondActivity : AppCompatActivity()
{
    private var binding: ActivityLoggedInBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoggedInBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }
}