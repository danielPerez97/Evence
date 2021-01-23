package projects.csce.evence.view.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import javax.inject.Inject;

import projects.csce.evence.BaseApplication;
import projects.csce.evence.databinding.ActivityLoggedInBinding;
import projects.csce.evence.di.loginscope.LoggedInModule;
import projects.csce.evence.utils.Utils;

public class SecondActivity extends AppCompatActivity
{
    private ActivityLoggedInBinding binding;
    @Inject GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.getAppComponent(this)
                .newLoggedInSubComponent(new LoggedInModule(((BaseApplication) getApplication()).getAccount()))
                .inject(this);
        super.onCreate(savedInstanceState);
        binding = ActivityLoggedInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        binding.setAccount(account);
    }
}
