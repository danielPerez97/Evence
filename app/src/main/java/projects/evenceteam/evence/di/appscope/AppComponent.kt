package projects.evenceteam.evence.di.appscope

import teamevence.evenceapp.fileselectview.di.FileSelectComponent
import teamevence.evenceapp.generateqrview.di.GenerateQRComponent
import teamevence.evenceapp.qrdialogview.di.QRDialogComponent
import projects.evenceteam.evence.BaseApplication
import projects.evenceteam.evence.view.ui.MainActivity
import projects.evenceteam.evence.view.ui.SettingsActivity

interface AppComponent
{
	fun inject(application: BaseApplication)
	fun inject(mainActivity: MainActivity)
	fun inject(activity: SettingsActivity)

	fun provideFileSelectFactory(): FileSelectComponent.Factory
	fun provideGenerateQrFactory(): GenerateQRComponent.Factory
	fun providerQrDialogFactory(): QRDialogComponent.Factory
}