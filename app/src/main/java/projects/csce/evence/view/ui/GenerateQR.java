package projects.csce.evence.view.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import javax.inject.Inject;

import projects.csce.evence.R;
import projects.csce.evence.databinding.ActivityGenerateQrBinding;
import projects.csce.evence.ical.EventSpec;
import projects.csce.evence.ical.ICalSpec;
import projects.csce.evence.service.model.event.Event;
import projects.csce.evence.service.model.qr.QrAttempt;
import projects.csce.evence.utils.Utils;
import projects.csce.evence.viewmodel.GenerateQrViewModel;

public class GenerateQR extends AppCompatActivity implements Observer<QrAttempt>
{
    private GenerateQrViewModel viewModel;
    private ActivityGenerateQrBinding binding;
    private Event currentEvent = new Event.Builder().build();
    @Inject ViewModelProvider.Factory viewModelFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Utils.getAppComponent(this).inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_generate_qr);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GenerateQrViewModel.class);
        binding.setView(this);
        binding.setViewModel(viewModel);

        viewModel.qrImages().observe(this, this);
    }

    public void onChanged(QrAttempt attempt)
    {
            if(attempt instanceof QrAttempt.Success)
            {
                Bitmap bitmap = ((QrAttempt.Success) attempt).getBitmap();
                binding.QRImage.setImageBitmap(bitmap);
            }
            else if(attempt instanceof QrAttempt.Failure)
            {
                Throwable e = ((QrAttempt.Failure) attempt).getE();
                e.printStackTrace();
                Utils.toastShort(getApplicationContext(), e.getLocalizedMessage());
            }
    }


    public void generateQR()
    {
        // Generate the .ics file
        int[] startMonthDayYear = Utils.toInts(binding.startDateTextView.getText().toString().split("/"));
        int[] endMonthDayYear = Utils.toInts(binding.endDateTextView.getText().toString().split("/"));
        EventSpec event = EventSpec.builder(0)
                .title(binding.titleEditText.getText().toString())
                .start(Utils.toZonedDateTime(startMonthDayYear))
                .end(Utils.toZonedDateTime(endMonthDayYear))
                .build();

        ICalSpec ical = ICalSpec.builder()
                .fileName(binding.titleEditText.getText().toString())
                .addEvent(event)
                .build();

        // Write the file to the file system
        viewModel.saveFile(ical);
    }

    public void startDateDialog()
    {
        CalendarDialog calendarDialog = new CalendarDialog(this, binding.startDateTextView);
        calendarDialog.dateDialog();
    }

    public void startTimeDialog() {
        CalendarDialog calendarDialog = new CalendarDialog(this, binding.startTimeTextView);
        calendarDialog.timeDialog();
    }

    public void endDateDialog() {
        CalendarDialog calendarDialog = new CalendarDialog(this, binding.endDateTextView);
        calendarDialog.dateDialog();
    }

    public void endTimeDialog() {
        CalendarDialog calendarDialog = new CalendarDialog(this, binding.endTimeTextView);
        calendarDialog.timeDialog();
    }

    public void saveQrButton() {
        Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
        viewModel.saveEvent(currentEvent);
    }

}
