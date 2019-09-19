package projects.csce.evence.di;

import com.squareup.moshi.Moshi;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

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
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(okHttpClient)
                .build();
    }
}
