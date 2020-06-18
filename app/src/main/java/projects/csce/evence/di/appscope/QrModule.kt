package projects.csce.evence.di.appscope

import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.Module
import dagger.Provides
import daniel.perez.core.DialogStarter
import daniel.perez.core.service.qr.QrBitmapGenerator
import javax.inject.Singleton

@Module
class QrModule(private val dialogStarter: DialogStarter)
{
	@Provides
	@Singleton
	fun provideEncoder(): BarcodeEncoder
	{
		return BarcodeEncoder()
	}

	@Provides
	@Singleton
	fun provideWriter(): MultiFormatWriter
	{
		return MultiFormatWriter()
	}

	@Provides
	@Singleton
	fun provideBitmapGenerator(writer: MultiFormatWriter, encoder: BarcodeEncoder): QrBitmapGenerator
	{
		return QrBitmapGenerator(writer, encoder)
	}

	@Provides
	@Singleton
	fun provideDialogStarter(): DialogStarter
	{
		return dialogStarter
	}
}