package projects.csce.evence.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import javax.inject.Inject;

import projects.csce.evence.R;
import projects.csce.evence.base.BaseApplication;
import projects.csce.evence.databinding.ActivityLoggedInBinding;
import projects.csce.evence.di.loggedin.LoggedInModule;
import projects.csce.evence.utils.Utils;

public class SecondActivity extends AppCompatActivity
{
    @Inject GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Utils.getAppComponent(this)
                .newLoggedInSubComponent( new LoggedInModule( ( (BaseApplication) getApplication() ).getAccount()) )
                .inject(this);
        super.onCreate(savedInstanceState);
        ActivityLoggedInBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_logged_in);
        binding.setAccount(account);
    }
}
