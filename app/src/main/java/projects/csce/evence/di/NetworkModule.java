package projects.csce.evence.di;

import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import projects.csce.evence.service.repository.DriveService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class NetworkModule
{
    @Provides
    @Singleton
    public Moshi provideMoshi()
    {
        return new Moshi.Builder().build();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkhttpClient()
    {
        return new OkHttpClient.Builder().build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Moshi moshi, OkHttpClient okHttpClient)
    {
        return new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public DriveService provideDriveService(Retrofit retrofit)
    {
        return retrofit.create(DriveService.class);
    }
}
