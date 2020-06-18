package daniel.perez.qrdialogview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.CalendarContract;
import android.view.LayoutInflater;

import javax.inject.Inject;

import daniel.perez.core.BaseActivity;
import daniel.perez.core.DialogClosable;
import daniel.perez.core.StartActivity;
import daniel.perez.core.model.ViewCalendarData;
import daniel.perez.core.model.ViewEvent;
import daniel.perez.core.service.FileManager;
import daniel.perez.qrdialogview.databinding.DialogBoxQrBinding;
import daniel.perez.qrdialogview.di.QRDialogComponentProvider;

import static android.content.Intent.ACTION_INSERT;

public class QRDialog
{
    private static final String TAG = "QRDialog";
    private Context context;
    private Dialog dialog;
    private ViewCalendarData ical;
    private ViewEvent currentEvent;
    private DialogBoxQrBinding binding;
    @Inject FileManager fileManager;
    @Inject StartActivity startActivity;


    public QRDialog(Context context, ViewCalendarData ical)
    {
        ((QRDialogComponentProvider) context.getApplicationContext())
                .provideQrDialogComponent()
                .inject(this);
        this.context = context;
        this.ical = ical;
        this.currentEvent = ical.getEvents().get(0);
        binding = DialogBoxQrBinding.inflate(LayoutInflater.from(context));

        binding.qrDialogEventTitleTextview.setText(currentEvent.getTitle());
        binding.qrDialogEventStartDateTextview.setText(currentEvent.getStartDate());
        binding.qrDialogEventStartTimeTextview.setText(currentEvent.getStartTime());
        binding.qrDialogEventLocationTextview.setText(currentEvent.getLocation());
        binding.qrDialogQrImageview.setImageBitmap(currentEvent.getImage());

        dialog = new Dialog(context);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();


        setupClicks();
    }

    public void setupClicks()
    {
        binding.closeDialogBtn.setOnClickListener(view -> closeDialog());
        binding.shareQrBtn.setOnClickListener(view -> shareQR());
        binding.importToCalendarBtn.setOnClickListener(view -> importToCalendar());
        binding.editBtn.setOnClickListener(view -> startActivity.startEditQr(context, fileManager.getFilePath(ical.getFileName())));
        binding.saveBtn.setOnClickListener(view -> save());
    }

    public void save( ) {
        // Bug the user about storing it
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/*");
        intent.putExtra(Intent.EXTRA_TITLE, currentEvent.getTitle() + ".ics");

        if (context instanceof BaseActivity)
        {
            ((BaseActivity) context).startActivityForResult(intent, 1);
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

    public void closeDialog() {
        if (context instanceof DialogClosable){
            ((DialogClosable) context).close();
        } else
            dialog.dismiss();
    }
}
