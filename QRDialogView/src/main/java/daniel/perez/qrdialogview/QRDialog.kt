package daniel.perez.qrdialogview

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.WindowManager
import coil.ImageLoader
import coil.load
import daniel.perez.core.ActivityStarter
import daniel.perez.core.BaseActivity
import daniel.perez.core.DialogClosable
import daniel.perez.core.RequestCodes
import daniel.perez.core.model.ViewEvent
import daniel.perez.qrdialogview.databinding.DialogBoxQrBinding

class QRDialog(
        private val context: Context,
        private val event: ViewEvent,
        private val imageLoader: ImageLoader,
        private val activityStarter: ActivityStarter
        )
{
    private val binding: DialogBoxQrBinding = DialogBoxQrBinding.inflate(LayoutInflater.from(context)).apply {
        qrDialogEventTitleTextview.text = event.title
        qrDialogEventStartDateTextview.text = event.startDatePretty()
        qrDialogEventStartTimeTextview.text = event.startDateTime.toLocalTime().toString()
        qrDialogEventLocationTextview.text = event.location
        qrDialogQrImageview.load(event.imageFileUri, imageLoader)
    }

    private val dialog: Dialog = Dialog(context).apply {
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        show()
    }

    init {
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
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/calendar"
            putExtra(Intent.EXTRA_TITLE, event.title + ".ics")
        }


        if (context is BaseActivity) {
            context.startActivityForResult(intent, RequestCodes.REQUEST_SAF)
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