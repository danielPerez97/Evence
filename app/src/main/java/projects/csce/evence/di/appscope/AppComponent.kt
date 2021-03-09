package projects.csce.evence.di.appscope

import daniel.perez.fileselectview.di.FileSelectComponent
import daniel.perez.generateqrview.di.GenerateQRComponent
import daniel.perez.qrdialogview.di.QRDialogComponent
import projects.csce.evence.BaseApplication
import projects.csce.evence.view.ui.MainActivity
import projects.csce.evence.view.ui.SettingsActivity

interface AppComponent
{
	fun inject(application: BaseApplication)
	fun inject(mainActivity: MainActivity)
	fun inject(activity: SettingsActivity)

	fun provideFileSelectFactory(): FileSelectComponent.Factory
	fun provideGenerateQrFactory(): GenerateQRComponent.Factory
	fun providerQrDialogFactory(): QRDialogComponent.Factory
}