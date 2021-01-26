package projects.csce.evence.view.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import projects.csce.evence.R
import projects.csce.evence.databinding.ActivitySettingsBinding
import projects.csce.evence.service.model.SharedPref
import projects.csce.evence.utils.getAppComponent
import javax.inject.Inject


class SettingsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
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
        //binding.darkModeSwitch.setOnClickListener { onDarkModeSwitchClicked() }
        binding.qrPreviewSwitch.setOnClickListener { onQrPreviewSwitchClicked() }


        ArrayAdapter.createFromResource(this, R.array.dark_mode_options_list, R.layout.spinner_text)
                .also { adapter ->
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    // Apply the adapter to the spinner
                    binding.darkModeSpinner.adapter = adapter}
        binding.darkModeSpinner.onItemSelectedListener = this

        setSavedSetting()
    }

    private fun setSavedSetting(){
        isDark = sharedPref.loadIntValue(getString(R.string.saved_dark_mode_setting), DarkModeOptions.LIGHT.ordinal)
        when (isDark) {
            DarkModeOptions.LIGHT.ordinal ->
                binding.darkModeSpinner.setSelection(DarkModeOptions.LIGHT.ordinal)
            DarkModeOptions.DARK.ordinal ->
                binding.darkModeSpinner.setSelection(DarkModeOptions.DARK.ordinal)
            else ->
                binding.darkModeSpinner.setSelection(DarkModeOptions.AUTO.ordinal)
        }

        isQrDisplayed = sharedPref.loadBooleanValue(getString(R.string.saved_qr_preview_setting), false)
        when(isQrDisplayed){
            true->
                binding.qrPreviewSwitch.isChecked = true
            else->
                binding.qrPreviewSwitch.isChecked = false
        }

    }

    private fun onBackClicked(){
        finish()
    }

    fun onQrPreviewSwitchClicked(){
        sharedPref.saveBooleanValue(getString(R.string.saved_qr_preview_setting), binding.qrPreviewSwitch.isChecked)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            DarkModeOptions.LIGHT.ordinal ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            DarkModeOptions.DARK.ordinal ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            DarkModeOptions.AUTO.ordinal ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        sharedPref.saveIntValue(getString(R.string.saved_dark_mode_setting), position)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    enum class DarkModeOptions{
        LIGHT,
        DARK,
        AUTO
    }

}