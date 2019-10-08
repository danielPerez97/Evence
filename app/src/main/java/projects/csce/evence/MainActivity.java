package projects.csce.evence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.squareup.moshi.Moshi;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import projects.csce.evence.http.RetrofitClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity
{

    @Inject Moshi moshi;
    @Inject Retrofit httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((BaseApplication) getApplication()).getInjector().inject(this);

        RetrofitClient client = httpClient.create(RetrofitClient.class);
       // client.postDataToServer("DATA");
    }

    //temporary
    public void onGenerateQrClick(View view) {
        Intent generateQRActivity = new Intent(this, GenerateQR.class);
        startActivity(generateQRActivity);
    }
}
