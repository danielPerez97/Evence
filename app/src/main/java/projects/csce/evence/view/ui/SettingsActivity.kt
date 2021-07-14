package projects.csce.evence.view.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.AndroidEntryPoint
import projects.csce.evence.R
import projects.csce.evence.databinding.ActivitySettingsBinding
import projects.csce.evence.service.model.SharedPref
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener
{
    @Inject lateinit var sharedPref:SharedPref
    private var isDark: Int = -99
    private var isDayMonthYear = false
    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { onBackClicked() }

        ArrayAdapter.createFromResource(this, R.array.dark_mode_options_list, R.layout.spinner_text)
                .also { adapter ->
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    // Apply the adapter to the spinner
                    binding.darkModeSpinner.adapter = adapter}
        binding.darkModeSpinner.onItemSelectedListener = this

        setSavedSetting()
    }

    private fun setSavedSetting()
    {
        isDark = sharedPref.loadIntValue(getString(R.string.saved_dark_mode_setting), DarkModeOptions.LIGHT.ordinal)
        when (isDark) {
            DarkModeOptions.LIGHT.ordinal ->
                binding.darkModeSpinner.setSelection(DarkModeOptions.LIGHT.ordinal)
            DarkModeOptions.DARK.ordinal ->
                binding.darkModeSpinner.setSelection(DarkModeOptions.DARK.ordinal)
            else ->
                binding.darkModeSpinner.setSelection(DarkModeOptions.AUTO.ordinal)
        }
    }

    private fun onBackClicked(){
        finish()
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