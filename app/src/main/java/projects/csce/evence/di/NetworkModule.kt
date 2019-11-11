package projects.csce.evence.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import projects.csce.evence.service.model.event.Event
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
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
	fun provideDummyData(): List<Event>
	{
		val dummyEvents = ArrayList<Event>()
		dummyEvents.add(Event.Builder().build())
		dummyEvents.add(Event.Builder().build())
		dummyEvents.add(Event.Builder().build())
		dummyEvents.add(Event.Builder().build())
		dummyEvents.add(Event.Builder().build())
		return dummyEvents
	}
}