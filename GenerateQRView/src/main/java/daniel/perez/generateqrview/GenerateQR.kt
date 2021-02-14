package daniel.perez.generateqrview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import daniel.perez.core.*
import daniel.perez.core.db.UiNewEvent
import daniel.perez.core.model.DateSetEvent
import daniel.perez.core.model.Half
import daniel.perez.core.model.TimeSetEvent
import daniel.perez.core.service.FileManager
import daniel.perez.generateqrview.databinding.ActivityGenerateQrBinding
import daniel.perez.generateqrview.di.GenerateQRComponentProvider
import daniel.perez.ical.ICalSpec
import daniel.perez.ical.Parser.parse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import okio.buffer
import okio.sink
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject

class GenerateQR : BaseActivity(), DialogClosable
{
    private lateinit var viewModel: GenerateQrViewModel
    private lateinit var binding: ActivityGenerateQrBinding
    private lateinit var currentEvent: ICalSpec
    private var startTime = TimeSetEvent(0, 0, Half.AM)
    private var endTime = TimeSetEvent(0, 0, Half.AM)
    private var startDate = DateSetEvent(1, 31, 1999)
    private var endDate = DateSetEvent(1, 31, 1999)
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var dialogStarter: DialogStarter
    @Inject lateinit var fileManager: FileManager

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
            currentEvent = ical
            Timber.i(ical.events[0].toString())
            fillInFields()
        }
        binding.startDateEditText.setOnClickListener { startDateDialog() }
        binding.startTimeEditText.setOnClickListener { startTimeDialog() }
        binding.endDateEditText.setOnClickListener { endDateDialog() }
        binding.endTimeEditText.setOnClickListener { endTimeDialog() }
        binding.addBtn.setOnClickListener { saveEvent() }
    }

    //fill in editText and other fields when event is being edited
    private fun fillInFields()
    {
        val event = currentEvent.events[0]

        //TODO("Implement the enum properly, calculate if its morning or after noon for AM, PM")
        startTime = TimeSetEvent(event.start.hour, event.start.minute, Half.AM)
        endTime = TimeSetEvent(event.end.hour, event.end.minute, Half.PM)
        startDate = DateSetEvent(event.start.monthValue, event.start.dayOfMonth, event.start.year)
        endDate = DateSetEvent(event.end.monthValue, event.end.dayOfMonth, event.end.year)

        binding.titleEditText.setText(event.title)
        binding.startDateTextView.text = event.getStartDate()
        binding.startTimeTextView.text = event.getStartTime()
        binding.endDateTextView.text = event.getEndFormatted("MM-dd-yyyy")
        binding.endTimeTextView.text = event.getEndFormatted("hh:mm a")
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
        viewModel.saveEvent(uiNewEvent)
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe { dialogStarter.startQrDialog(this, it) }
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
                    LocalDateTime.parse("${startDate.string()}T${startTime.string()}" ),
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
                fileManager.writeFileActionCreateDocument(this, currentEvent, data)
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

    override fun close()
    {
        finish()
    }
}