package projects.csce.evence.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import daniel.perez.core.BaseActivity
import daniel.perez.core.DialogStarter
import daniel.perez.core.StartActivity
import daniel.perez.core.adapter.CardsAdapter
import daniel.perez.core.di.ViewModelFactory
import daniel.perez.core.model.UiPreference
import daniel.perez.core.model.ViewCalendarData
import daniel.perez.core.service.FileManager
import daniel.perez.core.service.qr.QrBitmapGenerator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import projects.csce.evence.BaseApplication
import projects.csce.evence.R
import projects.csce.evence.databinding.ActivityMainBinding
import projects.csce.evence.service.model.SharedPref
import projects.csce.evence.utils.getAppComponent
import projects.csce.evence.viewmodel.MainViewModel
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity() {
    private val RC_SIGN_IN = 1
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var eventsAdapter: CardsAdapter

    @Inject lateinit var fileManager: FileManager
    @Inject lateinit var signInClient: GoogleSignInClient
    @Inject lateinit var factory: ViewModelFactory
    @Inject lateinit var generator: QrBitmapGenerator
    @Inject lateinit var dialogStarter: DialogStarter
    @Inject lateinit var startActivity: StartActivity
    @Inject lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        this.getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)

        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)

        eventsAdapter = CardsAdapter(this)
        handleRecyclerView()

        //handle drawer
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerMain,
                binding.toolbarMain,
                R.string.app_name,
                R.string.app_name)
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer)
        binding.drawerMain.addDrawerListener(actionBarDrawerToggle)
        binding.qrBtn.setOnClickListener { unit: View? -> startQrActivity() }
        actionBarDrawerToggle.syncState()
        setupSubscriptions()
    }

    private fun setupSubscriptions() {
        disposables.add(viewModel.liveFiles()
                .doOnSubscribe { event: Disposable? -> fileManager.notifyIcals() }
                .subscribe { files: List<ViewCalendarData> ->
                    Timber.i("Received Files")
                    eventsAdapter.setData(files)
                })
        disposables.add(eventsAdapter.clicks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { viewCalendarData: ViewCalendarData? ->
                    dialogStarter.startQrDialog(this, viewCalendarData!!)
                })
        disposables.add(sharedPref.getUiPref()
                .doOnSubscribe { pref: Disposable? -> sharedPref.notifyUiPref() }
                .subscribe { uiPref: UiPreference? -> eventsAdapter.updateUiFormat(uiPref) })
        Timber.i("Set up subscriptions")
    }

    override fun onResume() {
        super.onResume()
        fileManager.notifyIcals()
        sharedPref.notifyUiPref()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.qr_camera_btn) {
            startQrReaderActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleRecyclerView() {
        binding.eventsRecyclerView.adapter = eventsAdapter
        binding.eventsRecyclerView.layoutManager = LinearLayoutManager(baseContext)
        disposables.add(viewModel.liveFiles().subscribe { events: List<ViewCalendarData> ->
            Timber.i(Integer.toString(events.size))
            eventsAdapter.onChanged(events)
            if (events.isEmpty()) binding.emptyTextview.visibility = View.VISIBLE else binding.emptyTextview.visibility = View.GONE
        })
    }

    fun startQrReaderActivity() {
        startActivity.startQrReader(this)
    }

    fun startQrActivity() {
        startActivity.startGenerateQr(this)
    }

    fun startSecondActivity() {
        startActivity.startSecondActivity(this)
    }

    fun startSettingsActivity(view: View?) {
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer)
        startActivity.startSettingsActivity(this)
    }

    fun startShareAppActivity(view: View?) {
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer)
        startActivity.startShareAppActivity(this)
    }

    fun startAboutActivity(view: View?) {
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer)
        startActivity.startAboutActivity(this)
    }

    fun signIn() {
        val signInIntent = signInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        Timber.d(Integer.toString(requestCode))
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            (application as BaseApplication).account = completedTask.getResult(ApiException::class.java)!!
            // Signed in successfully, show authenticated UI.
            startSecondActivity()
        } catch (e: ApiException) {
            Timber.tag("SigninAttempt").w(Integer.toString(e.statusCode))
            e.printStackTrace()
            Toast.makeText(applicationContext, "Sign-In Failed", Toast.LENGTH_SHORT).show()
        }
    }
}