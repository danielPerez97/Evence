package projects.csce.evence.di.appscope

import dagger.Component
import daniel.perez.fileselectview.di.FileSelectComponent
import daniel.perez.generateqrview.di.GenerateQRComponent
import daniel.perez.qrcameraview.di.QrReaderComponent
import daniel.perez.qrdialogview.di.QRDialogComponent
import projects.csce.evence.BaseApplication
import projects.csce.evence.di.loginscope.LoggedInModule
import projects.csce.evence.di.loginscope.LoggedInSubComponent
import projects.csce.evence.view.ui.MainActivity
import projects.csce.evence.view.ui.SettingsActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, LoginModule::class, QrModule::class, ViewModelModule::class, SettingsModule::class, QrReaderModule::class])
interface AppComponent
{
	fun inject(application: BaseApplication)
	fun inject(mainActivity: MainActivity)
	fun inject(activity: SettingsActivity)
	fun newLoggedInSubComponent(module: LoggedInModule): LoggedInSubComponent

	fun provideFileSelectFactory(): FileSelectComponent.Factory
	fun provideGenerateQrFactory(): GenerateQRComponent.Factory
	fun provideQrReaderFactory(): QrReaderComponent.Factory
	fun providerQrDialogFactory(): QRDialogComponent.Factory
}