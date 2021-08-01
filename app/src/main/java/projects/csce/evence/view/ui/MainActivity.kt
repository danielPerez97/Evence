package projects.csce.evence.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Switch
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import daniel.perez.core.*
import daniel.perez.core.RequestCodes.REQUEST_SAF
import daniel.perez.core.adapter.CardsAdapter
import daniel.perez.core.model.ViewEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import projects.csce.evence.R
import projects.csce.evence.databinding.ActivityMainBinding
import projects.csce.evence.service.model.SharedPref
import projects.csce.evence.viewmodel.MainViewModel
import projects.csce.evence.viewmodel.SortState
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(), AdapterView.OnItemSelectedListener
{
    private lateinit var currentEvent: ViewEvent
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var eventsAdapter: CardsAdapter

    @Inject lateinit var dialogStarter: DialogStarter
    @Inject lateinit var activityStarter: ActivityStarter
    @Inject lateinit var sharedPref: SharedPref
    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var activityResultActions: ActivityResultActions

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setTheme(R.style.AppTheme)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewSetup()
        setupSubscriptions()
    }

    private fun viewSetup()
    {
        binding.qrBtn.setOnClickListener {
//            activityStarter.startNewEventActivity(this)
            activityStarter.startGenerateQr(this)
        }

        //apply custom toolbar
        setSupportActionBar(binding.toolbarMain)

        eventsAdapter = CardsAdapter()

        //handle drawer
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerMain, binding.toolbarMain, R.string.app_name, R.string.app_name)
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer)
        binding.drawerMain.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // RecyclerView
        binding.eventsRecyclerView.adapter = eventsAdapter
        binding.eventsRecyclerView.layoutManager = LinearLayoutManager(baseContext)

    }

    private fun setupSubscriptions()
    {
        disposables += viewModel.events()
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe{ events: List<ViewEvent> ->
                    eventsAdapter.setData( events )
                    if (events.isEmpty())
                    {
                        binding.emptyTextview.visibility = View.VISIBLE
                    }
                    else
                    {
                        binding.emptyTextview.visibility = View.GONE
                    }
                }

        disposables += binding.searchEditText.textChanges()
                .debounce(100, TimeUnit.MILLISECONDS)
                .map { it.toString() }
                .distinctUntilChanged()
                .subscribe {
                    Timber.i("""Text Change Subscribe: '$it'""")
                    viewModel.newSearch( it.toString() )
                }

        disposables += eventsAdapter.clicks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    currentEvent = it
                    dialogStarter.startQrDialog(this, it)
                }

        disposables += sharedPref.getUiPref()
                .doOnSubscribe { pref: Disposable? -> sharedPref.notifyUiPref() }
                .subscribe { eventsAdapter.updateUiFormat(it) }
    }

    override fun onResume()
    {
        super.onResume()
        sharedPref.notifyUiPref()
    }

    override fun onStop()
    {
        super.onStop()
        binding.searchEditText.setText("")
    }

    override fun onDestroy()
    {
        super.onDestroy()
        disposables.dispose()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == R.id.qr_camera_btn) {
            activityStarter.startQrReader(this)
        } else if (item.itemId == R.id.sort_btn) {
            handleSort()
        }
        return super.onOptionsItemSelected(item)
    }

    fun handleSort() {
        val popupMenu = PopupMenu(this, findViewById(R.id.sort_btn))
        popupMenu.menuInflater.inflate(R.menu.menu_sort, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort_by_creation ->
                {
                    viewModel.setSortState(SortState.RECENTLY_CREATED)
                }
                R.id.sort_by_date ->
                {
                    viewModel.setSortState(SortState.BY_DATE_ASCENDING)
                }
            }
            true
        }
        popupMenu.show()
    }

    fun startSettingsActivity(view: View?)
    {
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer)
        activityStarter.startSettingsActivity(this)
    }

    fun startShareAppActivity(view: View?)
    {
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer)
        activityStarter.startShareAppActivity(this)
    }

    fun startAboutActivity(view: View?)
    {
        binding.drawerMain.closeDrawer(binding.includedDrawer.navigationDrawer)
        activityStarter.startAboutActivity(this)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        Timber.i("${RequestCodes.map(requestCode)}, requestCode: $requestCode")
        if(resultCode == RESULT_OK)
        {
            Timber.i("RESULT_OK")
            if (requestCode == REQUEST_SAF)
            {
                if(data != null)
                {
                    disposables += activityResultActions.actionCreateDocumentEvent(this, currentEvent, data)
                            .subscribe(resultHandler)
                }
            }
        }
    }

    private val resultHandler = Consumer<ActionResult> {
        when(it)
        {
            ActionResult.Success -> toastShort("Wrote File Successfully")
            is ActionResult.Failure ->
            {
                Timber.e(it.t)
                toastShort("Error writing file")
            }
            ActionResult.InTransit ->
            {
                Timber.i("Writing file...")
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}