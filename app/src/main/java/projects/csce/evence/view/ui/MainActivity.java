package projects.csce.evence.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import daniel.perez.core.BaseActivity;
import daniel.perez.core.DialogStarter;
import daniel.perez.core.StartActivity;
import daniel.perez.core.adapter.CardsAdapter;
import daniel.perez.core.di.ViewModelFactory;
import daniel.perez.core.service.FileManager;
import daniel.perez.core.service.qr.QrBitmapGenerator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import projects.csce.evence.BaseApplication;
import projects.csce.evence.R;
import projects.csce.evence.databinding.ActivityMainBinding;
import projects.csce.evence.utils.Utils;
import projects.csce.evence.viewmodel.MainViewModel;
import timber.log.Timber;

public class MainActivity extends BaseActivity
{
    private int RC_SIGN_IN = 1;
    private MainViewModel viewModel;
    private ActivityMainBinding binding;
    private CardsAdapter eventsAdapter;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Inject FileManager fileManager;
    @Inject GoogleSignInClient signInClient;
    @Inject ViewModelFactory factory;
    @Inject QrBitmapGenerator generator;
    @Inject DialogStarter dialogStarter;
    @Inject StartActivity startActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        Utils.getAppComponent(this).inject(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.qrBtn.setOnClickListener( unit -> startQrActivity());

        //apply custom toolbar
        setSupportActionBar(binding.toolbarMain);


        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        eventsAdapter = new CardsAdapter(this);
        handleRecyclerView();

        //binding.loginBtn.setOnClickListener(view -> signIn());

        //handle drawer
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerMain, binding.toolbarMain, R.string.app_name, R.string.app_name);
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer);
        binding.drawerMain.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setupSubscriptions();
    }

    private void setupSubscriptions()
    {
        disposables.add(viewModel.liveFiles()
                .doOnSubscribe(event -> fileManager.notifyIcals())
                .subscribe(files -> {
                    Timber.i("Received Files");
                    eventsAdapter.setData(files);
                }));

        disposables.add(eventsAdapter.clicks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewCalendarData -> dialogStarter.startQrDialog(this, viewCalendarData)));

        Timber.i("Set up subscriptions");
    }

    @Override
    protected void onResume() {
        super.onResume();
        fileManager.notifyIcals();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        disposables.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.qr_camera_btn)
        {
            startQrReaderActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleRecyclerView() {
        binding.eventsRecyclerView.setAdapter(eventsAdapter);
        binding.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        disposables.add(viewModel.liveFiles().subscribe( events -> {
            Timber.i(Integer.toString(events.size()));
            eventsAdapter.onChanged(events);
            if (events.size() == 0)
                binding.emptyTextview.setVisibility(View.VISIBLE);
            else
                binding.emptyTextview.setVisibility(View.GONE);
        }));
    }

    public void startQrReaderActivity()
    {
        startActivity.startQrReader(this);
    }

    public void startQrActivity() {
        startActivity.startGenerateQr(this);
    }

    public void startSecondActivity() {
        startActivity.startSecondActivity(this);
    }

    public void startSettingsActivity(View view) {
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer);
        startActivity.startSettingsActivity(this);
    }

    public void startShareAppActivity(View view) {
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer);
        startActivity.startShareAppActivity(this);
    }

    public void startAboutActivity(View view){
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer);
        startActivity.startAboutActivity(this);
    }

    public void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        Timber.d(Integer.toString(requestCode));
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
            Timber.tag("SigninAttempt").w(Integer.toString(e.getStatusCode()));
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Sign-In Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
