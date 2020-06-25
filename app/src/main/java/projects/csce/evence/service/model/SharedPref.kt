package projects.csce.evence.service.model

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import projects.csce.evence.R

class SharedPref(val context : Context ) {
    private val sharedPref = context.getSharedPreferences(context.getString(R.string.saved_setting_shared_pref_name),Context.MODE_PRIVATE)

    fun loadIntValue(key:String, defaultValue: Int) = sharedPref.getInt(key, defaultValue)
    fun loadBooleanValue(key: String, defaultValue: Boolean) = sharedPref.getBoolean(key, defaultValue)

    fun saveIntValue(key : String, value : Int){
        with (sharedPref.edit()) {
            putInt(key, value)
            commit()
        }
    }

    fun saveBooleanValue(key : String, value : Boolean){
        with (sharedPref.edit()) {
            putBoolean(key, value)
            commit()
        }
    }

    fun setSavedPreferences(){
        val isDark = loadIntValue(context.getString(R.string.saved_dark_mode_setting), Configuration.UI_MODE_NIGHT_NO)
        when (isDark) {
            Configuration.UI_MODE_NIGHT_YES ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Configuration.UI_MODE_NIGHT_NO ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}