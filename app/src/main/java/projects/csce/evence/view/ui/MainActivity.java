package projects.csce.evence.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import projects.csce.evence.BaseApplication;
import projects.csce.evence.R;
import projects.csce.evence.databinding.ActivityMainBinding;
import projects.csce.evence.di.viewmodel.ViewModelFactory;
import projects.csce.evence.utils.Utils;
import projects.csce.evence.view.adapter.CardsAdapter;
import projects.csce.evence.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private int RC_SIGN_IN = 1;
    private MainViewModel viewModel;
    private SignInButton signInButton;
    private ActivityMainBinding binding;

    @Inject GoogleSignInClient signInClient;
    @Inject ViewModelFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.getAppComponent(this).inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setView(this);
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        handleRecyclerView();
        //viewModel.getEventsList().observe(this, data -> adapter.setData(data));
        signInButton = findViewById(R.id.login_btn);
        signInButton.setOnClickListener(view -> signIn());
        Utils.toastLong(getBaseContext(), "Hello");
    }

    private void handleRecyclerView() {
        CardsAdapter eventsAdapter = new CardsAdapter(this, viewModel.generateDummyData());

        //for testing purposes only. populating the recyclerview with dummy data
        eventsAdapter.setData(viewModel.generateDummyData());

        binding.eventsRecyclerView.setAdapter(eventsAdapter);
        binding.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        /** todo: fix
         * viewModel.getEventsList().observe(this, new Observer<List<Event>>() {
        @Override public void onChanged(List<Event> events) {
        eventsAdapter.setData(events);
        }
        });**/
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
