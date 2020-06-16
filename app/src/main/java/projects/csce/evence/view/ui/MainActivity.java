package projects.csce.evence.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

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

public class MainActivity extends AppCompatActivity  {

    private int RC_SIGN_IN = 1;
    private MainViewModel viewModel;
    private ActivityMainBinding binding;
    private CardsAdapter eventsAdapter;

    @Inject
    FileManager fileManager;
    @Inject
    GoogleSignInClient signInClient;
    @Inject
    ViewModelFactory factory;
    @Inject
    QrBitmapGenerator generator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
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

        //binding.loginBtn.setOnClickListener(view -> signIn());

        //handle drawer
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerMain, binding.toolbarMain, R.string.app_name, R.string.app_name);
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer);
        binding.drawerMain.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fileManager.notifyIcals();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.qr_camera_btn:
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleRecyclerView() {
        binding.eventsRecyclerView.setAdapter(eventsAdapter);
        binding.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        viewModel.liveFiles().observe(this, events -> {
            Log.i("SUBSCRIBER", Integer.toString(events.size()));
            eventsAdapter.onChanged(events);
            if (events.size() == 0)
                binding.emptyTextview.setVisibility(View.VISIBLE);
            else
                binding.emptyTextview.setVisibility(View.GONE);
        });
    }

    public void startQrActivity() {
        Intent generateQRActivity = new Intent(this, GenerateQR.class);
        startActivity(generateQRActivity);
    }

    public void startSecondActivity() {
        Intent secondActivityIntent = new Intent(this, SecondActivity.class);
        startActivity(secondActivityIntent);
    }

    public void startSettingsActivity(View view) {
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer);
        Intent settingsActivity = new Intent(this, SettingsActivity.class);
        startActivity(settingsActivity);
    }

    public void startShareAppActivity(View view) {
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer);
        Intent shareActivity = new Intent(this, ShareAppActivity.class);
        startActivity(shareActivity);
    }

    public void startAboutActivity(View view){
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer);
        Intent aboutActivity = new Intent(this, AboutActivity.class);
        startActivity(aboutActivity);
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
