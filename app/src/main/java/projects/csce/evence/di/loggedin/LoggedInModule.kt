package projects.csce.evence.di.loggedin

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.Module
import dagger.Provides

@Module
class LoggedInModule(private val account: GoogleSignInAccount)
{
	@Provides
	@SignedInScope
	fun provideAccount(): GoogleSignInAccount = account

}