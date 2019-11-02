package projects.csce.evence.di.loggedin;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.jetbrains.annotations.NotNull;

import dagger.Module;
import dagger.Provides;

@Module
public class LoggedInModule
{
    private GoogleSignInAccount account;

    public LoggedInModule(@NotNull GoogleSignInAccount account)
    {
        this.account = account;
    }

    @Provides
    @SignedInScope
    public GoogleSignInAccount provideAccount()
    {
        return account;
    }
}
