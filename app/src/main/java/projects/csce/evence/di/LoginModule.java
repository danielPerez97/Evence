package projects.csce.evence.di;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import projects.csce.evence.R;

@Module
public class LoginModule
{
    private Context appContext;

    public LoginModule(Context appContext)
    {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    GoogleSignInOptions provideSignInOptions()
    {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(appContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    @Provides
    @Singleton
    GoogleSignInClient provideSignInClient(GoogleSignInOptions signInOptions)
    {
        return GoogleSignIn.getClient(appContext, signInOptions);
    }
}
