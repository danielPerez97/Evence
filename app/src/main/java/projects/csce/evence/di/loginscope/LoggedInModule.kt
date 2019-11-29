package projects.csce.evence.di.loginscope

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.Module
import dagger.Provides
import projects.csce.evence.di.loginscope.SignedInScope

@Module
class LoggedInModule(private val account: GoogleSignInAccount)
{
	@Provides
	@SignedInScope
	fun provideAccount(): GoogleSignInAccount = account

}