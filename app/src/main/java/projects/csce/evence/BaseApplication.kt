package projects.csce.evence

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import daniel.perez.core.service.FileManager
import daniel.perez.generateqrview.di.GenerateQRComponent
import daniel.perez.generateqrview.di.GenerateQRComponentProvider
import daniel.perez.qrcameraview.di.QrReaderComponent
import daniel.perez.qrcameraview.di.QrReaderComponentProvider
import daniel.perez.qrdialogview.di.QRDialogComponent
import daniel.perez.qrdialogview.di.QRDialogComponentProvider
import projects.csce.evence.di.appscope.AppComponent
import projects.csce.evence.di.appscope.DaggerAppComponent
import projects.csce.evence.di.appscope.DatabaseModule
import projects.csce.evence.di.appscope.LoginModule
import projects.csce.evence.service.model.SharedPref
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

class BaseApplication : Application(), QrReaderComponentProvider, GenerateQRComponentProvider, QRDialogComponentProvider {
    lateinit var injector: AppComponent
    lateinit var account: GoogleSignInAccount

    @JvmField
    @Inject
    var fileManager: FileManager? = null
    override fun onCreate() {
        super.onCreate()

        // Setup Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        injector = DaggerAppComponent.builder()
                .loginModule(LoginModule(applicationContext))
                .databaseModule( DatabaseModule(applicationContext) )
                .build()
        injector.inject(this)
        SharedPref(applicationContext).setSavedPreferences()
    }

    override fun getQrReaderComponent(): QrReaderComponent
    {
        return injector.provideQrReaderFactory().create()
    }

    override fun provideGenerateQRComponent(): GenerateQRComponent
    {
        return injector.provideGenerateQrFactory().create()
    }

    override fun provideQrDialogComponent(): QRDialogComponent
    {
        return injector.providerQrDialogFactory().create()
    }
}