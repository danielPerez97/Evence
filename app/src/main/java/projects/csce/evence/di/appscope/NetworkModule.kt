package projects.csce.evence.di.appscope

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import daniel.perez.core.service.FileManager
import daniel.perez.core.service.qr.QrBitmapGenerator
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule
{
	@Provides
	@Singleton
	fun provideMoshi(): Moshi
	{
		return Moshi.Builder().build()
	}

	@Provides
	@Singleton
	fun provideOkhttpClient(): OkHttpClient
	{
		return OkHttpClient.Builder().build()
	}

	@Provides
	@Singleton
	fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit
	{
		return Retrofit.Builder()
				.baseUrl("https://www.googleapis.com")
				.addConverterFactory(MoshiConverterFactory.create(moshi))
				.addConverterFactory(ScalarsConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
				.client(okHttpClient)
				.build()
	}

	@Provides
	@Singleton
	fun provideFileManager(context: Context, generator: QrBitmapGenerator): FileManager
	{
		return FileManager(context, generator)
	}
}