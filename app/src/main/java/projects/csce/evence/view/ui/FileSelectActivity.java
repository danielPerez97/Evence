package projects.csce.evence.view.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import projects.csce.evence.R;
import projects.csce.evence.service.model.FileManager;
import projects.csce.evence.service.model.qr.QrBitmapGenerator;
import projects.csce.evence.utils.Utils;
import projects.csce.evence.view.adapter.CardsAdapter;

public class FileSelectActivity extends AppCompatActivity
{
    @Inject FileManager fileManager;
    @Inject QrBitmapGenerator generator;
    private RecyclerView fileSelector;
    private CardsAdapter adapter;
    private Uri fileUri;
    private Intent resultIntent;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState)
    {
        Utils.getAppComponent(this).inject(this);
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_file_select);
        resultIntent = new Intent("projects.csce.evence.ACTION_RETURN_FILE");
        setResult(AppCompatActivity.RESULT_CANCELED, null);

        adapter = new CardsAdapter(this, generator, fileManager);
        fileSelector = findViewById(R.id.file_selector);
        fileSelector.setAdapter(adapter);


        adapter.setSelectionListener(ical ->
        {
            fileUri = fileManager.getFileUri(ical.getFileName());
            if(fileUri != null)
            {
                // Grant temporary read permission
                resultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Pur the Uri and MIME type in the result Intent
                resultIntent.setDataAndType(fileUri, getContentResolver().getType(fileUri));

                // Set the result
                setResult(AppCompatActivity.RESULT_OK, resultIntent);
            }
            else
            {
                resultIntent.setDataAndType(null, "");
                setResult(RESULT_CANCELED, resultIntent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.setSelectionListener(null);
    }
}
