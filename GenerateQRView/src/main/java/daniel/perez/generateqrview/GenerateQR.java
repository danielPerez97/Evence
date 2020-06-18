package daniel.perez.generateqrview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import javax.inject.Inject;

import daniel.perez.core.BaseActivity;
import daniel.perez.core.DialogClosable;
import daniel.perez.core.DialogStarter;
import daniel.perez.core.Utils;
import daniel.perez.generateqrview.databinding.ActivityGenerateQrBinding;
import daniel.perez.generateqrview.di.GenerateQRComponentProvider;
import daniel.perez.ical.EventSpec;
import daniel.perez.ical.ICalSpec;
import daniel.perez.ical.Parser;
import io.reactivex.functions.Consumer;
import okio.BufferedSink;
import okio.Okio;
import daniel.perez.core.service.FileManager;
import daniel.perez.core.service.qr.QrAttempt;
import daniel.perez.core.service.qr.QrBitmapGenerator;
import daniel.perez.core.model.ViewCalendarData;
import daniel.perez.core.model.ViewEvent;

public class GenerateQR extends BaseActivity implements Consumer<QrAttempt>, DialogClosable
{
    private GenerateQrViewModel viewModel;
    private ActivityGenerateQrBinding binding;
    private ICalSpec currentEvent;
    @Inject ViewModelProvider.Factory viewModelFactory;
    @Inject QrBitmapGenerator generator;
    @Inject FileManager fileManager;
    @Inject DialogStarter dialogStarter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((GenerateQRComponentProvider) getApplication())
                .provideGenerateQRComponent()
                .inject(this);
        super.onCreate(savedInstanceState);
        binding = ActivityGenerateQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GenerateQrViewModel.class);

        viewModel.qrImages()
                .subscribe(this);

        if(getIntent() != null && getIntent().getStringExtra("FILE_PATH") != null)
        {
            File file = new File(getIntent().getStringExtra("FILE_PATH"));
            ICalSpec ical = Parser.INSTANCE.parse(file);
            currentEvent = ical;
            Log.i("ICALFILEPATH", ical.getEvents().get(0).toString());
            fillInFields();
        }

        binding.startDateEditText.setOnClickListener(view -> startDateDialog());
        binding.startTimeEditText.setOnClickListener(view -> startTimeDialog());
        binding.endDateEditText.setOnClickListener(view -> endDateDialog());
        binding.endTimeEditText.setOnClickListener(view -> endTimeDialog());
        binding.addBtn.setOnClickListener(view -> generateQR());
    }

    //fill in editText and other fields when event is being edited
    public void fillInFields(){
        EventSpec event = currentEvent.getEvents().get(0);
        binding.titleEditText.setText(event.getTitle());
        binding.startDateTextView.setText(event.getStartDate());
        binding.startTimeTextView.setText(event.getStartTime());
        binding.endDateTextView.setText(event.getEndFormatted("MM-dd-yyyy"));
        binding.endTimeTextView.setText(event.getEndFormatted("hh:mm a"));
        binding.locationEditText.setText(event.getLocation());
        binding.descriptionEditText.setText(event.getDescription());
    }

    @Override
    public void accept(QrAttempt attempt) {
        if (attempt instanceof QrAttempt.Success) {
            Bitmap bitmap = ((QrAttempt.Success) attempt).getBitmap();
            //binding.QRImage.setImageBitmap(bitmap);
        } else if (attempt instanceof QrAttempt.Failure) {
            Throwable e = ((QrAttempt.Failure) attempt).getE();
            e.printStackTrace();
            Utils.toastShort(getApplicationContext(), Objects.requireNonNull(e.getLocalizedMessage()));
        }
    }

    public void generateQR() {
        // Handle the dates
        int[] startMonthDayYear = Utils.toInts(binding.startDateTextView.getText().toString().split("/"));
        int[] endMonthDayYear = Utils.toInts(binding.endDateTextView.getText().toString().split("/"));

        // Handle the hours and minutes
        int[] startHourMinutes = Utils.toInts(binding.startTimeTextView.getText().toString().split(":"));
        int[] endHourMinutes = Utils.toInts(binding.endTimeTextView.getText().toString().split(":"));

        EventSpec event = new EventSpec.Builder(0)
                .title(binding.titleEditText.getText().toString())
                .description(binding.descriptionEditText.getText().toString())
                .location(binding.locationEditText.getText().toString())
                .start(Utils.toZonedDateTime(startMonthDayYear, startHourMinutes))
                .end(Utils.toZonedDateTime(endMonthDayYear, endHourMinutes))
                .build();

        currentEvent = new ICalSpec.Builder()
                .fileName(binding.titleEditText.getText().toString())
                .addEvent(event)
                .build();

        // Write the file to the file system
        viewModel.saveFile(currentEvent);

        ViewEvent viewEvent = new ViewEvent(event.getTitle(),
                event.getDescription(),
                event.getStartTime(),
                event.getStartDate(),
                event.getStartInstantEpoch(),
                event.getEndEpochMilli(),
                event.getLocation(),
                event.text(),
                generator.forceGenerate(event.text())
        );
        ViewCalendarData calendar = new ViewCalendarData(currentEvent.getFileName(), Collections.singletonList(viewEvent));
        ((DialogStarter) getApplication()).startQrDialog(this, calendar);
    }

    // Handle the user choosing a place to store the file
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Uri uri;
            if (data != null) {
                uri = data.getData();
                try {
                    ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "w");

                    FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
                    try (BufferedSink sink = Okio.buffer(Okio.sink(fileOutputStream))) {
                        sink.writeUtf8(currentEvent.text());
                    } catch (IOException e) {
                        Log.i("FILEWRITER", e.getMessage());
                        e.printStackTrace();
                    }
                    pfd.close();
                } catch (FileNotFoundException e) {
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
        dialogStarter.startDateDialog(this, binding.startDateTextView);
    }

    public void startTimeDialog()
    {
        dialogStarter.startTimeDialog(this, binding.startTimeTextView);
    }

    public void endDateDialog()
    {
        dialogStarter.startDateDialog(this, binding.endDateTextView);
    }

    public void endTimeDialog()
    {
        dialogStarter.startTimeDialog(this, binding.endTimeTextView);
    }


    @Override
    public void close()
    {
        finish();
    }
}
