package projects.csce.evence.view.ui

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import projects.csce.evence.R
import projects.csce.evence.databinding.ActivitySettingsBinding
import projects.csce.evence.service.model.SharedPref
import projects.csce.evence.utils.getAppComponent
import javax.inject.Inject


class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    private var isDark: Int = -99
    private var isDayMonthYear = false
    private var isQrDisplayed = true

    @Inject
    lateinit var sharedPref:SharedPref

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { onBackClicked() }
        binding.darkModeSwitch.setOnClickListener { onDarkModeSwitchClicked() }
        binding.qrPreviewSwitch.setOnClickListener { onQrPreviewSwitchClicked() }


        setSavedSetting()
    }

    fun setSavedSetting(){
        isDark = sharedPref.loadIntValue(getString(R.string.saved_dark_mode_setting), Configuration.UI_MODE_NIGHT_NO)
        when (isDark) {
            Configuration.UI_MODE_NIGHT_YES ->
                binding.darkModeSwitch.isChecked = true
            Configuration.UI_MODE_NIGHT_NO ->
                binding.darkModeSwitch.isChecked = false
            else ->
                binding.darkModeSwitch.isChecked = false
        }

        isQrDisplayed = sharedPref.loadBooleanValue(getString(R.string.saved_qr_preview_setting), false)
        when(isQrDisplayed){
            true->
                binding.qrPreviewSwitch.isChecked = true
            else->
                binding.qrPreviewSwitch.isChecked = false
        }

    }

    fun onBackClicked(){
        finish()
    }

    fun onDarkModeSwitchClicked(){
        isDark = sharedPref.loadIntValue(getString(R.string.saved_dark_mode_setting), Configuration.UI_MODE_NIGHT_NO)
        when (isDark) {
            Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                isDark = Configuration.UI_MODE_NIGHT_NO
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                isDark = Configuration.UI_MODE_NIGHT_YES
            }
        }
        sharedPref.saveIntValue(getString(R.string.saved_dark_mode_setting), isDark)
    }

    fun onQrPreviewSwitchClicked(){
        sharedPref.saveBooleanValue(getString(R.string.saved_qr_preview_setting), binding.qrPreviewSwitch.isChecked)
    }

}