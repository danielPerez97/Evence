package projects.csce.evence.service.model

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import daniel.perez.core.model.UiPreference
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import projects.csce.evence.R

class SharedPref(val context : Context ) {
    private val sharedPref = context.getSharedPreferences(context.getString(R.string.saved_setting_shared_pref_name),Context.MODE_PRIVATE)
    private val uiPrefProcessor: PublishSubject<UiPreference> = PublishSubject.create()

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
        val isDark = loadIntValue(context.getString(R.string.saved_dark_mode_setting), DarkModeOptions.LIGHT.ordinal)
        when (isDark) {
            DarkModeOptions.LIGHT.ordinal ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            DarkModeOptions.DARK.ordinal ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            DarkModeOptions.AUTO.ordinal ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    fun notifyUiPref() {
        val isQrPreviewed = loadBooleanValue(context.getString(R.string.saved_qr_preview_setting), false)
        uiPrefProcessor.onNext(UiPreference(isQrPreviewed))
    }

    fun getUiPref() : Observable<UiPreference> {
        return uiPrefProcessor
    }

    enum class DarkModeOptions{
        LIGHT,
        DARK,
        AUTO
    }

}