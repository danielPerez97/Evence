package projects.evenceteam.evence.di.appscope

import android.content.Context
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule
{
//	@Provides
//	@Singleton
//	fun provideMoshi(): Moshi
//	{
//		return Moshi.Builder().build()
//	}
//
//	@Provides
//	@Singleton
//	fun provideOkhttpClient(
//			@ApplicationContext appContext: Context
//	): OkHttpClient
//	{
//		val megaBytes: Long = 10 * 1024 * 1024
//		return OkHttpClient.Builder()
//				.cache( Cache(appContext.cacheDir, megaBytes) )
//				.build()
//	}
//
	@Provides
	@Singleton
	fun provideCoilImageLoader(
			@ApplicationContext appContext: Context
	): ImageLoader
	{
		return ImageLoader.Builder(appContext)
				.availableMemoryPercentage(0.25)
				.crossfade(true)
				.build()
	}
//
//	@Provides
//	@Singleton
//	fun provideRetrofit(
//			moshi: Moshi,
//			okHttpClient: OkHttpClient): Retrofit
//	{
//		return Retrofit.Builder()
//				.baseUrl("https://www.googleapis.com")
//				.addConverterFactory(MoshiConverterFactory.create(moshi))
//				.addConverterFactory(ScalarsConverterFactory.create())
//				.addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
//				.client(okHttpClient)
//				.build()
//	}
//
//	@Provides
//	@Singleton
//	fun provideFileManager(
//			@ApplicationContext appContext: Context,
//			generator: QrBitmapGenerator
//	): FileManager
//	{
//		return FileManager(appContext, generator)
//	}
}