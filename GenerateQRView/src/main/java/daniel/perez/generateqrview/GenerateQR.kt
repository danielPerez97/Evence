package daniel.perez.generateqrview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxbinding4.view.clicks
import daniel.perez.core.*
import daniel.perez.core.db.UiNewEvent
import daniel.perez.core.db.toViewEvent
import daniel.perez.core.model.DateSetEvent
import daniel.perez.core.model.Half
import daniel.perez.core.model.TimeSetEvent
import daniel.perez.core.model.ViewEvent
import daniel.perez.generateqrview.databinding.ActivityGenerateQrBinding
import daniel.perez.generateqrview.di.GenerateQRComponentProvider
import daniel.perez.ical.Parser.parse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

class GenerateQR : BaseActivity(), DialogClosable, AdapterView.OnItemSelectedListener
{
    private lateinit var viewModel: GenerateQrViewModel
    private lateinit var binding: ActivityGenerateQrBinding
    private lateinit var currentEvent: ViewEvent
    private var startTime = TimeSetEvent(0, 0, Half.AM)
    private var endTime = TimeSetEvent(0, 0, Half.AM)
    private var startDate = DateSetEvent(1, 31, 1999)
    private var endDate = DateSetEvent(1, 31, 1999)
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var dialogStarter: DialogStarter
    @Inject lateinit var activityResultActions: ActivityResultActions

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        (application as GenerateQRComponentProvider)
                .provideGenerateQRComponent()
                .inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateQrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GenerateQrViewModel::class.java)

        if (intent != null && intent.getStringExtra("FILE_PATH") != null)
        {
            val file = File(intent.getStringExtra("FILE_PATH"))
            val ical = parse(file)
//            currentEvent = ical
            Timber.i(ical.events[0].toString())
            fillInFields()
        }

        ArrayAdapter.createFromResource(this, R.array.event_recurrence_options_list, R.layout.spinner_text)
                .also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.recurrenceSpinner.adapter = adapter}
        binding.recurrenceSpinner.onItemSelectedListener = this
        disposables += binding.startDateEditText.clicks().subscribe { startDateDialog() }
        binding.startTimeEditText.clicks().subscribe { startTimeDialog() }
        binding.endDateEditText.clicks().subscribe { endDateDialog() }
        binding.endTimeEditText.clicks().subscribe { endTimeDialog() }
        binding.addBtn.clicks().subscribe { saveEvent() }
    }



    //fill in editText and other fields when event is being edited
    private fun fillInFields()
    {
        val event = currentEvent

        //TODO("Implement the enum properly, calculate if its morning or after noon for AM, PM")
        startTime = TimeSetEvent(event.startDateTime.hour, event.startDateTime.minute, Half.AM)
        endTime = TimeSetEvent(event.endDateTime.hour, event.endDateTime.minute, Half.PM)
        startDate = DateSetEvent(event.startDateTime.monthValue, event.startDateTime.dayOfMonth, event.startDateTime.year)
        endDate = DateSetEvent(event.endDateTime.monthValue, event.endDateTime.dayOfMonth, event.endDateTime.year)

        binding.titleEditText.setText(event.title)
        binding.startDateTextView.text = event.startDatePretty()
        binding.startTimeTextView.text = event.startTimePretty()
        binding.endDateTextView.text = event.endDatePretty()
        binding.endTimeTextView.text = event.endTimePretty()
        binding.locationEditText.setText(event.location)
        binding.descriptionEditText.setText(event.description)
    }

    private fun areValidFields() : Boolean {
        if (binding.titleEditText.text.isBlank()) {
            applicationContext.snackbarShort(binding.titleEditText, getString(R.string.enter_valid_title))
            return false
        }

        if (binding.startDateTextView.text.toString().equals(getString(R.string.select_date))) {
            applicationContext.snackbarShort(binding.startDateTextView, getString(R.string.enter_valid_start_date))
            return false
        }

        //if end date is empty, set it to be the same as start date
        if (binding.endDateTextView.text.toString().equals(getString(R.string.select_date))) {
            endDate = startDate
        }

        return true
    }

    private fun saveEvent()
    {
        if (!areValidFields()) {
            return
        }
        val uiNewEvent: UiNewEvent = extractEventFromUi()
        disposables += viewModel.saveEvent(uiNewEvent)
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe {
                    currentEvent = it.toViewEvent()
                    dialogStarter.startQrDialog(this, it.toViewEvent())
                }
    }

    private fun extractEventFromUi(): UiNewEvent
    {
        Timber.i("Start_Date: ${startDate.string()}")
        Timber.i("Start_Time: $startTime")
        Timber.i("End_Date: ${endDate.string()}")
        Timber.i("End_Time: $endTime")
        val uiNewEvent: UiNewEvent
        with(binding)
        {
            uiNewEvent = UiNewEvent(
                    titleEditText.text.toString(),
                    descriptionEditText.text.toString(),
                    locationEditText.text.toString(),
                    LocalDateTime.parse("${startDate.string()}T${startTime.string()}" ), //todo add method into Utils.kt
                    LocalDateTime.parse("${endDate.string()}T${endTime.string()}"),
            )
        }

        return uiNewEvent
    }

    // Handle the user choosing a place to store the file
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodes.REQUEST_SAF)
        {
            if (data != null)
            {
                disposables += activityResultActions.actionCreateDocumentEvent(this, currentEvent, data)
                        .subscribe {
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
            }
        }
    }

    private fun startDateDialog()
    {
        disposables.add(dialogStarter.startDateDialog(this)
                .subscribe {
                    startDate = it
                    binding.startDateTextView.text = "${it.month}-${it.dayOfMonth}-${it.year}"
                })
    }

    private fun endDateDialog()
    {
        disposables.add( dialogStarter.startDateDialog( this )
                .subscribe {
                    endDate = it
                    binding.endDateTextView.text = "${it.month}-${it.dayOfMonth}-${it.year}"

                } )
    }

    private fun startTimeDialog()
    {
        disposables.add(dialogStarter.startTimeDialog(this)
                .subscribe {
                    startTime = it
                    binding.startTimeTextView.text = getAMPMTimeFormat("${it.hour} ${it.minute}")
                } )
    }

    private fun endTimeDialog()
    {
        disposables.add(dialogStarter.startTimeDialog(this)
                .subscribe {
                    endTime = it
                    binding.endTimeTextView.text = getAMPMTimeFormat("${it.hour} ${it.minute}")
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun close()
    {
        finish()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}