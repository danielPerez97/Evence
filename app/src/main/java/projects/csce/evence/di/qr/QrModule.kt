package projects.csce.evence.di.qr

import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.Module
import dagger.Provides
import projects.csce.evence.service.model.qr.QrBitmapGenerator
import javax.inject.Singleton

@Module
class QrModule
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
}