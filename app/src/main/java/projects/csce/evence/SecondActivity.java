package projects.csce.evence;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

public class SecondActivity extends AppCompatActivity
{
    @Inject
    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((BaseApplication) getApplication()).getInjector().inject(this);

        client.authenticator();
    }
}
