package projects.csce.evence.di

import dagger.Component
import projects.csce.evence.di.loggedin.LoggedInModule
import projects.csce.evence.di.loggedin.LoggedInSubComponent
import projects.csce.evence.di.qr.QrModule
import projects.csce.evence.view.ui.GenerateQR
import projects.csce.evence.view.ui.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, LoginModule::class, QrModule::class, ViewModelModule::class])
interface AppComponent
{
	fun inject(mainActivity: MainActivity)
	fun inject(generateQR: GenerateQR)
	fun newLoggedInSubComponent(module: LoggedInModule): LoggedInSubComponent
}