package projects.csce.evence.view.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import org.threeten.bp.format.DateTimeFormatter;

import projects.csce.evence.R;
import projects.csce.evence.databinding.DialogBoxQrBinding;
import projects.csce.evence.ical.EventSpec;
import projects.csce.evence.ical.ICalSpec;
import projects.csce.evence.service.model.qr.QrBitmapGenerator;

import static android.content.Intent.ACTION_INSERT;

public class QRDialog {
    private static final String TAG = "QRDialog";
    private Context context;
    private Dialog dialog;
    private EventSpec currentEvent;
    DialogBoxQrBinding binding;
    private QrBitmapGenerator generator;


    public QRDialog(Context context, EventSpec currentEvent, QrBitmapGenerator generator) {
        this.context = context;
        this.currentEvent = currentEvent;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_box_qr, null, false);
        this.generator = generator;
        binding.setView(this);

        binding.qrDialogEventTitleTextview.setText(currentEvent.getTitle());
        binding.qrDialogEventStartDateTextview.setText(currentEvent.getStart().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        binding.qrDialogEventStartTimeTextview.setText(currentEvent.getStart().format(DateTimeFormatter.ofPattern("hh:mm a")));
        binding.qrDialogEventLocationTextview.setText(currentEvent.getLocation());
        binding.qrDialogQrImageview.setImageBitmap(generator.forceGenerate(currentEvent));

        dialog = new Dialog(context);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        Log.d(TAG, "share:");


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, generator.forceGenerate(currentEvent));
        //context.startActivity(intent);
    }

    public void importToCalendar() {
        Toast.makeText(context, "import", Toast.LENGTH_SHORT).show();
        Intent toCalendar = new Intent(ACTION_INSERT);
        toCalendar.setData(CalendarContract.Events.CONTENT_URI);
        toCalendar.putExtra(CalendarContract.Events.TITLE, currentEvent.getTitle());
        toCalendar.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, currentEvent.getStart().toInstant().toEpochMilli());
        toCalendar.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, currentEvent.getEnd().toInstant().toEpochMilli());
        toCalendar.putExtra(CalendarContract.Events.EVENT_LOCATION, currentEvent.getLocation());
        toCalendar.putExtra(CalendarContract.Events.DESCRIPTION, currentEvent.getDescription());
        context.startActivity(toCalendar);
    }
}
