package projects.csce.evence.view.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import projects.csce.evence.R
import projects.csce.evence.databinding.ActivitySettingsBinding
import projects.csce.evence.utils.getAppComponent
import javax.inject.Inject


class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    private var isDark: Int = -99

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        binding.view = this
        binding.lifecycleOwner = this

        isDark = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (isDark) {
            Configuration.UI_MODE_NIGHT_YES ->
                binding.darkModeSwitch.isChecked = true
            Configuration.UI_MODE_NIGHT_NO ->
                binding.darkModeSwitch.isChecked = false
        }
    }

    fun onBackClicked(){
        onBackPressed()
    }

    fun onDarkModeSwitchClicked(){
        when (isDark) {
            Configuration.UI_MODE_NIGHT_YES ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Configuration.UI_MODE_NIGHT_NO ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}