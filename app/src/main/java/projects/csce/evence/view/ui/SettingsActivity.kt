package projects.csce.evence.view.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import projects.csce.evence.R
import projects.csce.evence.databinding.ActivitySettingsBinding
import projects.csce.evence.utils.getAppComponent
import javax.inject.Inject


class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        binding.view = this
        binding.lifecycleOwner = this
        //viewModel = ViewModelProviders.of(this, viewModelFactory).get(QrReaderViewModel::class.java)
    }

    fun onBackClicked(){
        onBackPressed()
    }
}