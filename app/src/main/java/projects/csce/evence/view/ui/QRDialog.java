package projects.csce.evence.view.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import org.threeten.bp.format.DateTimeFormatter;

import projects.csce.evence.R;
import projects.csce.evence.databinding.DialogBoxQrBinding;
import projects.csce.evence.ical.EventSpec;
import projects.csce.evence.ical.ICalSpec;

public class QRDialog {
    private static final String TAG = "QRDialog";
    private Context context;
    private Dialog dialog;
    private EventSpec currentEvent;
    DialogBoxQrBinding binding;


    public QRDialog(Context context, EventSpec currentEvent){
        this.context = context;
        this.currentEvent = currentEvent;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_box_qr, null, false);
        binding.setView(this);

        binding.qrDialogEventTitleTextview.setText(currentEvent.getTitle());
        binding.qrDialogEventStartDateTextview.setText(currentEvent.getStart().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));

        binding.qrDialogEventStartTimeTextview.setText(currentEvent.getStart().format(DateTimeFormatter.ofPattern("HH:mm a")));

        dialog = new Dialog(context);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    protected void save(ICalSpec iCalSpec) {
        // Bug the user about storing it
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/*");
        intent.putExtra(Intent.EXTRA_TITLE, iCalSpec.getFileName() + ".ics");

        if (context instanceof GenerateQR) {
            ((GenerateQR) context).startActivityForResult(intent, 1);
        }
    }

    public void shareQR() {
        Log.d(TAG, "share:");
    }
}
