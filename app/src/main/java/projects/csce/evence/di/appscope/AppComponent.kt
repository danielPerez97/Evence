package projects.csce.evence.di.appscope

import dagger.Component
import projects.csce.evence.di.loginscope.LoggedInModule
import projects.csce.evence.di.loginscope.LoggedInSubComponent
import projects.csce.evence.view.ui.FileSelectActivity
import projects.csce.evence.view.ui.GenerateQR
import projects.csce.evence.view.ui.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, LoginModule::class, QrModule::class, ViewModelModule::class])
interface AppComponent
{
	fun inject(mainActivity: MainActivity)
	fun inject(generateQR: GenerateQR)
	fun inject(activity: FileSelectActivity)
	fun newLoggedInSubComponent(module: LoggedInModule): LoggedInSubComponent
}