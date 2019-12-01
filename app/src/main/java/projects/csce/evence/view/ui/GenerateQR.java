package projects.csce.evence.view.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;

import okio.BufferedSink;
import okio.Okio;
import projects.csce.evence.R;
import projects.csce.evence.databinding.ActivityGenerateQrBinding;
import projects.csce.evence.ical.EventSpec;
import projects.csce.evence.ical.ICalSpec;
import projects.csce.evence.service.model.qr.QrAttempt;
import projects.csce.evence.utils.Utils;
import projects.csce.evence.viewmodel.GenerateQrViewModel;

public class GenerateQR extends AppCompatActivity implements Observer<QrAttempt>
{
    private GenerateQrViewModel viewModel;
    private ActivityGenerateQrBinding binding;
    private ICalSpec currentEvent;
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
                Utils.toastShort(getApplicationContext(), Objects.requireNonNull(e.getLocalizedMessage()));
            }
    }


    public void generateQR()
    {
        // Generate the .ics file
        int[] startMonthDayYear = Utils.toInts(binding.startDateTextView.getText().toString().split("/"));
        int[] endMonthDayYear = Utils.toInts(binding.endDateTextView.getText().toString().split("/"));
        EventSpec event = new EventSpec.Builder(0)
                .title(binding.titleEditText.getText().toString())
                .start(Utils.toZonedDateTime(startMonthDayYear))
                .end(Utils.toZonedDateTime(endMonthDayYear))
                .build();

        currentEvent = new ICalSpec.Builder()
                .fileName(binding.titleEditText.getText().toString())
                .addEvent(event)
                .build();

        // Write the file to the file system
        viewModel.saveFile(currentEvent);

        // Bug the user about storing it
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/*");
        intent.putExtra(Intent.EXTRA_TITLE, currentEvent.getFileName() + ".ics");
        startActivityForResult(intent, 1);

    }

    // Handle the user choosing a place to store the file
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Uri uri;
            if (data != null) {
                uri = data.getData();
                try
                {
                    ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "w");

                    FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
                    try(BufferedSink sink = Okio.buffer(Okio.sink(fileOutputStream)))
                    {
                        sink.writeUtf8(currentEvent.text());
                    }
                    catch (IOException e)
                    {
                        Log.i("FILEWRITER", e.getMessage());
                        e.printStackTrace();
                    }
                    pfd.close();
                }
                catch (FileNotFoundException e)
                {
                    Log.i("FILEWRITER", e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("FILEWRITER", "Wrote File");
            }
        }


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
    }

}
