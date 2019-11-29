package projects.csce.evence.di.appscope

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import projects.csce.evence.R
import javax.inject.Singleton

@Module
class LoginModule(private val appContext: Context)
{
	@Provides
	@Singleton
	fun provideContext(): Context
	{
		return appContext
	}

	@Provides
	@Singleton
	fun provideSignInOptions(): GoogleSignInOptions
	{
		return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(appContext.getString(R.string.default_web_client_id))
				.requestEmail()
				.build()
	}

	@Provides
	@Singleton
	fun provideSignInClient(signInOptions: GoogleSignInOptions): GoogleSignInClient
	{
		return GoogleSignIn.getClient(appContext, signInOptions)
	}

}