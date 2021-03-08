package projects.csce.evence.di.appscope

import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import daniel.perez.core.service.qr.QrBitmapGenerator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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