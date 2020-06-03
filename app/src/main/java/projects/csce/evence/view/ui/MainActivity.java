package projects.csce.evence.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import projects.csce.evence.BaseApplication;
import projects.csce.evence.R;
import projects.csce.evence.databinding.ActivityMainBinding;
import projects.csce.evence.di.viewmodel.ViewModelFactory;
import projects.csce.evence.service.model.FileManager;
import projects.csce.evence.service.model.qr.QrBitmapGenerator;
import projects.csce.evence.utils.Utils;
import projects.csce.evence.view.adapter.CardsAdapter;
import projects.csce.evence.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private int RC_SIGN_IN = 1;
    private MainViewModel viewModel;
    private ActivityMainBinding binding;
    private CardsAdapter eventsAdapter;

    @Inject FileManager fileManager;
    @Inject GoogleSignInClient signInClient;
    @Inject ViewModelFactory factory;
    @Inject QrBitmapGenerator generator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.getAppComponent(this).inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setView(this);
        binding.setLifecycleOwner(this);

        //apply custom toolbar
        setSupportActionBar(binding.toolbarMain);

        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        eventsAdapter = new CardsAdapter(this, generator, fileManager);
        handleRecyclerView();

        binding.loginBtn.setOnClickListener(view -> signIn());

    }

    @Override
    protected void onResume() {
        super.onResume();
        fileManager.notifyIcals();
    }

    private void handleRecyclerView() {

        //for testing purposes only. populating the recyclerview with dummy data
        binding.eventsRecyclerView.setAdapter(eventsAdapter);
        binding.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        viewModel.liveFiles().observe(this, events -> {
            Log.i("SUBSCRIBER", Integer.toString(events.size()));
            eventsAdapter.onChanged(events);
        });
    }

    public void startQrReaderActivity(){
        Intent generateQRActivity = new Intent(this, QrReaderActivity.class);
        startActivity(generateQRActivity);
    }

    public void startQrActivity() {
        Intent generateQRActivity = new Intent(this, GenerateQR.class);
        startActivity(generateQRActivity);
    }

    public void startSecondActivity() {
        Intent secondActivityIntent = new Intent(this, SecondActivity.class);
        startActivity(secondActivityIntent);
    }

    public void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        Log.d("Google", Integer.toString(requestCode));
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            ((BaseApplication) getApplication()).setAccount(completedTask.getResult(ApiException.class));
            // Signed in successfully, show authenticated UI.
            startSecondActivity();

        } catch (ApiException e) {
            Log.w("SigninAttempt", Integer.toString(e.getStatusCode()));
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Sign-In Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
