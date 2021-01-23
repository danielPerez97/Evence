package projects.csce.evence.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import projects.csce.evence.BaseApplication
import projects.csce.evence.databinding.ActivityLoggedInBinding
import projects.csce.evence.di.loginscope.LoggedInModule
import projects.csce.evence.utils.getAppComponent
import javax.inject.Inject

class SecondActivity : AppCompatActivity() {
    private var binding: ActivityLoggedInBinding? = null

    @JvmField
    @Inject
    var account: GoogleSignInAccount? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        this.getAppComponent()
                .newLoggedInSubComponent(LoggedInModule((application as BaseApplication).account))
                .inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityLoggedInBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        //        binding.setAccount(account);
    }
}