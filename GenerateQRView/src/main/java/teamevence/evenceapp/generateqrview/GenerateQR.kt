package teamevence.evenceapp.generateqrview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import teamevence.evenceapp.core.*
import teamevence.evenceapp.core.db.UiNewEvent
import teamevence.evenceapp.core.db.toViewEvent
import teamevence.evenceapp.core.model.DateSetEvent
import teamevence.evenceapp.core.model.Half
import teamevence.evenceapp.core.model.TimeSetEvent
import teamevence.evenceapp.core.model.ViewEvent
import teamevence.evenceapp.generateqrview.databinding.ActivityGenerateQrBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class GenerateQR : BaseActivity(), DialogClosable, AdapterView.OnItemSelectedListener {
    private lateinit var viewModel: GenerateQrViewModel
    private lateinit var binding: ActivityGenerateQrBinding
    private lateinit var currentEvent: ViewEvent
    private var startTime = TimeSetEvent(0, 0, Half.AM)
    private var endTime = TimeSetEvent(0, 0, Half.AM)
    private var startDate = DateSetEvent(1, 31, 1999)
    private var endDate = DateSetEvent(1, 31, 1999)
    @Inject
    lateinit var dialogStarter: DialogStarter
    @Inject
    lateinit var activityResultActions: ActivityResultActions
    private var isEditing = false

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateQrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(GenerateQrViewModel::class.java)

        if (intent != null && intent.getLongExtra("EVENT_ID", -1L) != -1L) {
            isEditing = true
            val id = intent.getLongExtra("EVENT_ID", -1)
            disposables += viewModel.getEvent(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        currentEvent = it
                        Timber.i(it.toString())
                        fillInFields()
                    }
        }

        /** for future updates. to set recurrence
        ArrayAdapter.createFromResource(this, R.array.event_recurrence_options_list, R.layout.spinner_text)
        .also { adapter ->
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.recurrenceSpinner.adapter = adapter
        }
        binding.recurrenceSpinner.onItemSelectedListener = this
         */

        disposables += binding.startDateEditText.clicks().subscribe { startDateDialog() }
        binding.startTimeEditText.clicks().subscribe { startTimeDialog() }
        binding.endDateEditText.clicks().subscribe { endDateDialog() }
        binding.endTimeEditText.clicks().subscribe { endTimeDialog() }
        binding.finishButton.clicks().subscribe {
            if (!isEditing) {
                saveEvent()
            } else {
                updateEvent()
            }
        }
    }

    //fill in editText and other fields when event is being edited
    private fun fillInFields() {
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

    private fun areValidFields(): Boolean {
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
        } else {
            if (endDate.year >= startDate.year
                    && endDate.month >= startDate.month
                    && endDate.dayOfMonth >= startDate.dayOfMonth
                    && endTime.hour >= startTime.hour
                    && endTime.minute >= startTime.minute) {
                return true
            }
            applicationContext.snackbarShort(binding.startDateTextView, getString(R.string.enter_valid_end_date_time))
            return false
        }

        return true
    }

    private fun saveEvent() {
        if (!areValidFields()) {
            return
        }
        val uiNewEvent: UiNewEvent = extractEventFromUi()
        disposables += viewModel.saveEvent(uiNewEvent)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    currentEvent = it.toViewEvent()
                    dialogStarter.startQrDialog(this, it.toViewEvent())
                }
    }

    private fun updateEvent() {
        if (!areValidFields()) {
            return
        }
        val uiNewEvent: UiNewEvent = extractEventFromUi()
        disposables += viewModel.updateEvent(currentEvent.id, uiNewEvent)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    currentEvent = it.toViewEvent()
                    dialogStarter.startQrDialog(this, it.toViewEvent())
                }
    }

    private fun extractEventFromUi(): UiNewEvent {
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
                    LocalDateTime.parse("${startDate.string()}T${startTime.string()}"), //todo add method into Utils.kt
                    LocalDateTime.parse("${endDate.string()}T${endTime.string()}"),
            )
        }

        return uiNewEvent
    }

    // Handle the user choosing a place to store the file
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodes.REQUEST_SAF) {
            if (data != null) {
                disposables += activityResultActions.actionCreateDocumentEvent(this, currentEvent, data)
                        .subscribe {
                            when (it) {
                                ActionResult.Success -> toastShort(this,"Wrote File Successfully")
                                is ActionResult.Failure -> {
                                    Timber.e(it.t)
                                    toastShort(this, "Error writing file")
                                }
                                ActionResult.InTransit -> {
                                    Timber.i("Writing file...")
                                }
                            }
                        }
            }
        }
    }

    private fun startDateDialog() {
        disposables.add(dialogStarter.startDateDialog(this)
                .subscribe {
                    startDate = it
                    binding.startDateTextView.text = "${it.month}-${it.dayOfMonth}-${it.year}"
                })
    }

    private fun endDateDialog() {
        disposables.add(dialogStarter.startDateDialog(this)
                .subscribe {
                    endDate = it
                    binding.endDateTextView.text = "${it.month}-${it.dayOfMonth}-${it.year}"

                })
    }

    private fun startTimeDialog() {
        disposables.add(dialogStarter.startTimeDialog(this)
                .subscribe {
                    startTime = it
                    binding.startTimeTextView.text = getAMPMTimeFormat("${it.hour} ${it.minute}")
                })
    }

    private fun endTimeDialog() {
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

    override fun close() {
        finish()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}