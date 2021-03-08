package projects.csce.evence.di.appscope

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import projects.csce.evence.R
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LoginModule
{

	@Provides
	@Singleton
	fun provideSignInOptions(
			@ApplicationContext appContext: Context
	): GoogleSignInOptions
	{
		return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(appContext.getString(R.string.default_web_client_id))
				.requestEmail()
				.build()
	}

	@Provides
	@Singleton
	fun provideSignInClient(
			@ApplicationContext appContext: Context,
			signInOptions: GoogleSignInOptions
	): GoogleSignInClient
	{
		return GoogleSignIn.getClient(appContext, signInOptions)
	}

}