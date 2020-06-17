package projects.csce.evence.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import projects.csce.evence.R
import projects.csce.evence.databinding.ActivityShareAppBinding

class ShareAppActivity : AppCompatActivity() {
    lateinit var binding: ActivityShareAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_app)
        binding.view = this
        binding.lifecycleOwner = this
    }

    fun shareAppQr(){
        //todo: share qr with playstore link
    }

    fun shareApp(){
        //todo: share playstore link directly
    }
}
