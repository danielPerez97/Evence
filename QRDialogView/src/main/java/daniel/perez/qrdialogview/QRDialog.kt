package daniel.perez.qrdialogview

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import daniel.perez.core.BaseActivity
import daniel.perez.core.DialogClosable
import daniel.perez.core.StartActivity
import daniel.perez.core.model.ViewCalendarData
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.service.FileManager
import daniel.perez.core.setLocaleDateFormat
import daniel.perez.qrdialogview.databinding.DialogBoxQrBinding
import daniel.perez.qrdialogview.di.QRDialogComponentProvider
import javax.inject.Inject

class QRDialog(context: Context, ical: ViewCalendarData) {
    private val context: Context
    private val dialog: Dialog
    private val ical: ViewCalendarData
    private val currentEvent: ViewEvent
    private val binding: DialogBoxQrBinding

    init {
        (context.applicationContext as QRDialogComponentProvider)
                .provideQrDialogComponent()
                .inject(this)
        this.context = context
        this.ical = ical
        currentEvent = ical.events[0]
        binding = DialogBoxQrBinding.inflate(LayoutInflater.from(context))
        binding.qrDialogEventTitleTextview.text = currentEvent.title
        binding.qrDialogEventStartDateTextview.text = setLocaleDateFormat(currentEvent.startDate)
        binding.qrDialogEventStartTimeTextview.text = currentEvent.startTime
        binding.qrDialogEventLocationTextview.text = currentEvent.location
        binding.qrDialogQrImageview.setImageBitmap(currentEvent.image)
        dialog = Dialog(context)
        dialog.setContentView(binding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        setupClicks()
    }

    @Inject
    lateinit var fileManager: FileManager

    @Inject
    lateinit var startActivity: StartActivity
    fun setupClicks() {
        binding.closeDialogBtn.setOnClickListener { view: View? -> closeDialog() }
        binding.shareQrBtn.setOnClickListener { view: View? -> shareQR() }
        binding.importToCalendarBtn.setOnClickListener { view: View? -> importToCalendar() }
        binding.saveBtn.setOnClickListener { view: View? -> save() }
        binding.editBtn.setOnClickListener { view: View? ->
            startActivity!!.startEditQr(context, ical)
            closeDialog()
        }
    }

    fun save() {
        // Bug the user about storing it
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/*"
        intent.putExtra(Intent.EXTRA_TITLE, currentEvent.title + ".ics")
        if (context is BaseActivity) {
            context.startActivityForResult(intent, 1)
        }
    }

    fun shareQR() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileManager!!.getFileUri("image_" + ical.fileName))
        shareIntent.type = "image/*"
        context.startActivity(Intent.createChooser(shareIntent, "Share images to.."))
    }

    fun importToCalendar() {
        val toCalendar = Intent(Intent.ACTION_INSERT)
        toCalendar.data = CalendarContract.Events.CONTENT_URI
        toCalendar.putExtra(CalendarContract.Events.TITLE, currentEvent.title)
        toCalendar.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, currentEvent.startInstantEpoch)
        toCalendar.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, currentEvent.endEpochMilli)
        toCalendar.putExtra(CalendarContract.Events.EVENT_LOCATION, currentEvent.location)
        toCalendar.putExtra(CalendarContract.Events.DESCRIPTION, currentEvent.description)
        context.startActivity(toCalendar)
    }

    fun closeDialog() {
        if (context is DialogClosable) {
            dialog.dismiss() //fixes window leak
            (context as DialogClosable).close()
        } else dialog.dismiss()
    }

    companion object {
        private const val TAG = "QRDialog"
    }

}