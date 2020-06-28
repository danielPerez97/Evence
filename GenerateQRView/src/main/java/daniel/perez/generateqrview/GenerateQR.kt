package daniel.perez.generateqrview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import daniel.perez.core.*
import daniel.perez.core.model.*
import daniel.perez.core.service.FileManager
import daniel.perez.core.service.qr.QrAttempt
import daniel.perez.core.service.qr.QrBitmapGenerator
import daniel.perez.generateqrview.databinding.ActivityGenerateQrBinding
import daniel.perez.generateqrview.di.GenerateQRComponentProvider
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec
import daniel.perez.ical.Parser.parse
import io.reactivex.functions.Consumer
import okio.buffer
import okio.sink
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.inject.Inject

class GenerateQR : BaseActivity(), Consumer<QrAttempt?>, DialogClosable
{
    private lateinit var viewModel: GenerateQrViewModel
    private lateinit var binding: ActivityGenerateQrBinding
    private lateinit var currentEvent: ICalSpec
    private var startTime = TimeSetEvent(0, 0, Half.AM)
    private var endTime = TimeSetEvent(0, 0, Half.AM)
    private var startDate = DateSetEvent(0, 0, 0)
    private var endDate = DateSetEvent(0, 0, 0)
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var generator: QrBitmapGenerator
    @Inject lateinit var fileManager: FileManager
    @Inject lateinit var dialogStarter: DialogStarter

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
        viewModel.qrImages()
                .subscribe(this)
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
        binding.addBtn.setOnClickListener { generateQR() }
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

    override fun accept(attempt: QrAttempt?)
    {
        if (attempt is QrAttempt.Success)
        {
            val bitmap = attempt.bitmap
            //binding.QRImage.setImageBitmap(bitmap);
        } else if (attempt is QrAttempt.Failure)
        {
            val e = attempt.e
            e.printStackTrace()
            applicationContext.toastShort(Objects.requireNonNull(e.localizedMessage))
        }
    }

    private fun generateQR()
    {
        // Handle the hours and minutes
        val event = EventSpec.Builder(0)
                .title(binding.titleEditText.text.toString())
                .description(binding.descriptionEditText.text.toString())
                .location(binding.locationEditText.text.toString())
                .start(toZonedDateTime(startDate.month, startDate.dayOfMonth, startDate.year, startTime.hour, startTime.minute))
                .end(toZonedDateTime(endDate.month, endDate.dayOfMonth, endDate.year, endTime.hour, endTime.minute))
                .build()

        currentEvent = ICalSpec.Builder()
                .fileName(binding.titleEditText.text.toString())
                .addEvent(event)
                .build()

        // Write the file to the file system
        viewModel.saveFile(currentEvent)
        val viewEvent = ViewEvent(event.title,
                event.description,
                event.getStartDate(),
                event.getStartTime(),
                event.getStartInstantEpoch(),
                event.getEndEpochMilli(),
                event.location,
                event.text(),
                generator.forceGenerate(event.text())
        )
        val calendar = ViewCalendarData(currentEvent.fileName, listOf(viewEvent))
        dialogStarter.startQrDialog(this, calendar)
    }

    // Handle the user choosing a place to store the file
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1)
        {
            val uri: Uri?
            if (data != null)
            {
                uri = data.data
                try
                {
                    val pfd = contentResolver.openFileDescriptor(uri!!, "w")
                    val fileOutputStream = FileOutputStream(pfd!!.fileDescriptor)
                    try
                    {
                        fileOutputStream.sink().buffer().use { sink -> sink.writeUtf8(currentEvent.text()) }
                    } catch (e: IOException)
                    {
                        Timber.i(e)
                        e.printStackTrace()
                    }
                    pfd.close()
                } catch (e: FileNotFoundException)
                {
                    Timber.i(e)
                    e.printStackTrace()
                } catch (e: IOException)
                {
                    e.printStackTrace()
                }
                Timber.i("Wrote File")
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
                    binding.startTimeTextView.text = "${it.hour}:${it.minute} ${it.half}"
                } )
    }

    private fun endTimeDialog()
    {
        disposables.add(dialogStarter.startTimeDialog(this)
                .subscribe {
                    endTime = it
                    binding.endTimeTextView.text = "${it.hour}:${it.minute} ${it.half}"
                })
    }

    override fun close()
    {
        finish()
    }
}