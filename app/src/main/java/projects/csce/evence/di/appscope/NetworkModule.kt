package projects.csce.evence.di.appscope

import android.content.Context
import android.widget.ImageView
import coil.ImageLoader
import coil.load
import coil.util.CoilUtils
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import daniel.perez.core.db.EventOps
import okhttp3.OkHttpClient
import daniel.perez.core.service.FileManager
import daniel.perez.core.service.qr.QrBitmapGenerator
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Cache
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule(private val appContext: Context )
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
		val megaBytes: Long = 10 *1024 * 1028
		return OkHttpClient.Builder()
				.cache( Cache(appContext.cacheDir, megaBytes) )
				.build()
	}

	@Provides
	@Singleton
	fun provideCoilImageLoader( okHttpClient: OkHttpClient ): ImageLoader
	{
		return ImageLoader.Builder(appContext)
				.availableMemoryPercentage(0.25)
				.crossfade(true)
				.okHttpClient(
						okHttpClient.newBuilder()
								.cache(CoilUtils.createDefaultCache(appContext))
								.build()
				)
				.build()
	}

	@Provides
	@Singleton
	fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit
	{
		return Retrofit.Builder()
				.baseUrl("https://www.googleapis.com")
				.addConverterFactory(MoshiConverterFactory.create(moshi))
				.addConverterFactory(ScalarsConverterFactory.create())
				.addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
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