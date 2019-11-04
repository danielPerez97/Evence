package projects.csce.evence.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import projects.csce.evence.R;
import projects.csce.evence.service.model.qr.QrAttempt;
import projects.csce.evence.service.model.qr.Qr;
import projects.csce.evence.utils.Utils;
import projects.csce.evence.viewmodel.GenerateQrViewModel;

public class GenerateQR extends AppCompatActivity
{
    private EditText title;
    private EditText startDate;
    private EditText endDate;
    private EditText location;
    private EditText description;
    private Button generator;
    private ImageView imageView;
    private GenerateQrViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);
        viewModel = ViewModelProviders.of(this).get(GenerateQrViewModel.class);
        title = findViewById(R.id.title_edit_text);
        startDate = findViewById(R.id.start_date_edit_text);
        endDate = findViewById(R.id.end_date_edit_text);
        location = findViewById(R.id.location_edit_text);
        description = findViewById(R.id.description_edit_text);
        generator = findViewById(R.id.generate_btn);
        imageView = findViewById(R.id.QRImage);

        generator.setOnClickListener(v -> viewModel.generateQrBitmap(generateQR()));


        viewModel.qrImages().observe(this, (QrAttempt attempt) -> {
            if(attempt instanceof QrAttempt.Success)
            {
                Bitmap bitmap = ((QrAttempt.Success) attempt).bitmap;
                imageView.setImageBitmap(bitmap);
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
                .title(title.getText().toString())
                .startDate(startDate.getText().toString())
                .endDate(endDate.getText().toString())
                .location(location.getText().toString())
                .description(description.getText().toString())
                .build();

    }
}
