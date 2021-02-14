package daniel.perez.qrdialogview

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.CalendarContract
import android.view.LayoutInflater
import coil.ImageLoader
import coil.load
import daniel.perez.core.BaseActivity
import daniel.perez.core.DialogClosable
import daniel.perez.core.ActivityStarter
import daniel.perez.core.model.ViewEvent
import daniel.perez.core.service.FileManager
import daniel.perez.qrdialogview.databinding.DialogBoxQrBinding
import daniel.perez.qrdialogview.di.QRDialogComponentProvider
import javax.inject.Inject

class QRDialog(context: Context, var event: ViewEvent) {
    private val context: Context
    private val dialog: Dialog
    private val binding: DialogBoxQrBinding
    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var fileManager: FileManager
    @Inject lateinit var activityStarter: ActivityStarter

    init {
        (context.applicationContext as QRDialogComponentProvider)
                .provideQrDialogComponent()
                .inject(this)
        this.context = context
        binding = DialogBoxQrBinding.inflate(LayoutInflater.from(context))
        binding.qrDialogEventTitleTextview.text = event.title
        binding.qrDialogEventStartDateTextview.text = event.startDatePretty()
        binding.qrDialogEventStartTimeTextview.text = event.startDateTime.toLocalTime().toString()
        binding.qrDialogEventLocationTextview.text = event.location
        binding.qrDialogQrImageview.load(event.imageFileUri, imageLoader)
        dialog = Dialog(context)
        dialog.setContentView(binding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        setupClicks()
    }

    private fun setupClicks() {
        binding.closeDialogBtn.setOnClickListener { closeDialog() }
        binding.shareQrBtn.setOnClickListener {  shareQR() }
        binding.importToCalendarBtn.setOnClickListener {  importToCalendar() }
        binding.saveBtn.setOnClickListener {  save() }
        binding.editBtn.setOnClickListener {
            activityStarter.startEditQr(context, event)
            closeDialog()
        }
    }

    private fun save() {
        // Bug the user about storing it
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("text/calendar")

        intent.putExtra(Intent.EXTRA_TITLE, event.title + ".ics")
        if (context is BaseActivity) {
            context.startActivityForResult(intent, 1)
        }
    }

    private fun shareQR() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, event.imageFileContentUri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share images to.."))
    }

    private fun importToCalendar() {
        val toCalendar = Intent(Intent.ACTION_INSERT)
        toCalendar.data = CalendarContract.Events.CONTENT_URI
        toCalendar.putExtra(CalendarContract.Events.TITLE, event.title)
        toCalendar.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.startEpochMilli())
        toCalendar.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.endEpochMilli())
        toCalendar.putExtra(CalendarContract.Events.EVENT_LOCATION, event.location)
        toCalendar.putExtra(CalendarContract.Events.DESCRIPTION, event.description)
        context.startActivity(toCalendar)
    }

    private fun closeDialog()
    {
        // Close the Dialog and the activity
        if (context is DialogClosable)
        {
            dialog.dismiss()
            (context as DialogClosable).close()
        }
        // Just close the Dialog
        else
        {
            dialog.dismiss()
        }
    }

    companion object {
        private const val TAG = "QRDialog"
    }

}