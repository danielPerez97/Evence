package projects.csce.evence.di.appscope

import dagger.Component
import projects.csce.evence.di.loginscope.LoggedInModule
import projects.csce.evence.di.loginscope.LoggedInSubComponent
import projects.csce.evence.view.ui.FileSelectActivity
import projects.csce.evence.view.ui.GenerateQR
import projects.csce.evence.view.ui.MainActivity
import projects.csce.evence.view.ui.QrReaderActivity
import projects.csce.evence.view.ui.SettingsActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, LoginModule::class, QrModule::class, ViewModelModule::class, SettingsModule::class])
interface AppComponent
{
	fun inject(mainActivity: MainActivity)
	fun inject(generateQR: GenerateQR)
	fun inject(activity: FileSelectActivity)
	fun inject(qrReaderActivity: QrReaderActivity)
	fun inject(activity: SettingsActivity)
	fun newLoggedInSubComponent(module: LoggedInModule): LoggedInSubComponent
}