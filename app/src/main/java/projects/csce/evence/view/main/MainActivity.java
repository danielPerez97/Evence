package projects.csce.evence.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import projects.csce.evence.R;
import projects.csce.evence.base.BaseApplication;
import projects.csce.evence.databinding.ActivityMainBinding;
import projects.csce.evence.utils.Utils;
import projects.csce.evence.view.GenerateQR;
import projects.csce.evence.view.SecondActivity;

public class MainActivity extends AppCompatActivity
{

    private int RC_SIGN_IN = 1;
    private MainViewModel viewModel;
    private CardsAdapter adapter;
    @Inject GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Utils.getAppComponent(this).inject(this);
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setView(this);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getCardData().observe(this, data -> adapter.setData(data));
    }

    public void startQrActivity()
    {
        Intent generateQRActivity = new Intent(this, GenerateQR.class);
        startActivity(generateQRActivity);
    }

    public void startSecondActivity()
    {
        Intent secondActivityIntent = new Intent(this, SecondActivity.class);
        startActivity(secondActivityIntent);
    }

    @OnClick(R.id.login_btn)
    public void signIn()
    {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        Log.d("Google", Integer.toString(requestCode));
        if (requestCode == RC_SIGN_IN)
        {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try
        {
            ((BaseApplication) getApplication()).setAccount(completedTask.getResult(ApiException.class));

            // Signed in successfully, show authenticated UI.
            startSecondActivity();

        } catch (ApiException e) {
            Log.w("SigninAttempt",  Integer.toString(e.getStatusCode()));
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Sign-In Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
