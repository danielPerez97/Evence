package projects.csce.evence.view.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.CalendarContract;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import projects.csce.evence.R;
import projects.csce.evence.databinding.DialogBoxQrBinding;
import projects.csce.evence.service.model.FileManager;
import projects.csce.evence.view.ui.model.ViewCalendarData;
import projects.csce.evence.view.ui.model.ViewEvent;

import static android.content.Intent.ACTION_INSERT;

public class QRDialog
{
    private static final String TAG = "QRDialog";
    private Context context;
    private Dialog dialog;
    private ViewCalendarData ical;
    private ViewEvent currentEvent;
    DialogBoxQrBinding binding;
    private FileManager fileManager;


    public QRDialog(Context context, ViewCalendarData ical, FileManager fileManager) {
        this.context = context;
        this.ical = ical;
        this.currentEvent = ical.getEvents().get(0);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_box_qr, null, false);
        this.fileManager = fileManager;
        binding.setView(this);

        binding.qrDialogEventTitleTextview.setText(currentEvent.getTitle());
        binding.qrDialogEventStartDateTextview.setText(currentEvent.getStartDate());
        binding.qrDialogEventStartTimeTextview.setText(currentEvent.getStartTime());
        binding.qrDialogEventLocationTextview.setText(currentEvent.getLocation());
        binding.qrDialogQrImageview.setImageBitmap(currentEvent.getImage());
        binding.editBtn.setOnClickListener(view -> {
            Intent intent = new Intent(context, GenerateQR.class);
            intent.putExtra("FILE_PATH", fileManager.getFilePath(ical.getFileName()));
            context.startActivity(intent);
        });

        dialog = new Dialog(context);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void save( ) {
        // Bug the user about storing it
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/*");
        intent.putExtra(Intent.EXTRA_TITLE, currentEvent.getTitle() + ".ics");

        if (context instanceof GenerateQR) {
            ((GenerateQR) context).startActivityForResult(intent, 1);
        } else {
            ((MainActivity) context).startActivityForResult(intent, 1);
        }
    }

    public void shareQR() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileManager.getFileUri("image_" + ical.getFileName()));
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "Share images to.."));
    }

    public void importToCalendar() {
        Intent toCalendar = new Intent(ACTION_INSERT);
        toCalendar.setData(CalendarContract.Events.CONTENT_URI);
        toCalendar.putExtra(CalendarContract.Events.TITLE, currentEvent.getTitle());
        toCalendar.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, currentEvent.getStartInstantEpoch());
        toCalendar.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, currentEvent.getEndEpochMilli());
        toCalendar.putExtra(CalendarContract.Events.EVENT_LOCATION, currentEvent.getLocation());
        toCalendar.putExtra(CalendarContract.Events.DESCRIPTION, currentEvent.getDescription());
        context.startActivity(toCalendar);
    }
}
