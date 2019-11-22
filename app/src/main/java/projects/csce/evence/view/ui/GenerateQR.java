package projects.csce.evence.view.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import javax.inject.Inject;

import projects.csce.evence.R;
import projects.csce.evence.databinding.ActivityGenerateQrBinding;
import projects.csce.evence.service.model.event.Event;
import projects.csce.evence.service.model.qr.QrAttempt;
import projects.csce.evence.utils.Utils;
import projects.csce.evence.viewmodel.GenerateQrViewModel;

public class   GenerateQR extends AppCompatActivity
{
    private GenerateQrViewModel viewModel;
    private ActivityGenerateQrBinding binding;
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

        viewModel.qrImages().observe(this, this::handleAttempts);
    }

    private void handleAttempts(QrAttempt attempt)
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

        Event qr =  new Event.Builder()
                .title(binding.titleEditText.getText().toString())
                .startDate(binding.startDateTextView.getText().toString())
                .startTime(binding.startTimeTextView.getText().toString())
                .endDate(binding.endDateTextView.getText().toString())
                .endTime(binding.endTimeTextView.getText().toString())
                .location(binding.locationEditText.getText().toString())
                .description(binding.descriptionEditText.getText().toString())
                .build();



        binding.getViewModel().generateQrBitmap(qr);

    }

    public void startDateDialog() {
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

    }

}
