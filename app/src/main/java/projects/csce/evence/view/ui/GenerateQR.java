package projects.csce.evence.view.ui;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import projects.csce.evence.R;
import projects.csce.evence.databinding.ActivityGenerateQrBindingImpl;
import projects.csce.evence.service.model.qr.Qr;
import projects.csce.evence.service.model.qr.QrAttempt;
import projects.csce.evence.utils.Utils;
import projects.csce.evence.viewmodel.GenerateQrViewModel;

public class GenerateQR extends AppCompatActivity
{
    private GenerateQrViewModel viewModel;
    private ActivityGenerateQrBindingImpl binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_generate_qr);
        binding.setView(this);
        viewModel = ViewModelProviders.of(this).get(GenerateQrViewModel.class);
        binding.setViewModel(viewModel);

        viewModel.qrImages().observe(this, attempt -> {
            if(attempt instanceof QrAttempt.Success)
            {
                Bitmap bitmap = ((QrAttempt.Success) attempt).bitmap;
                binding.QRImage.setImageBitmap(bitmap);
            }
            else if(attempt instanceof QrAttempt.Failure)
            {
                Throwable e = ((QrAttempt.Failure) attempt).e;
                e.printStackTrace();
                Utils.toastShort(getApplicationContext(), e.getMessage());
            }
        });
    }

    public Qr generateQR()
    {
        return new Qr.Builder()
                .title(binding.titleEditText.getText().toString())
                .startDate(binding.startDateEditText.getText().toString())
                .endDate(binding.endDateEditText.getText().toString())
                .location(binding.locationEditText.getText().toString())
                .description(binding.descriptionEditText.getText().toString())
                .build();

    }
}
