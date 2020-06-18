package daniel.perez.fileselectview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import daniel.perez.core.adapter.CardsAdapter;
import daniel.perez.core.service.FileManager;
import daniel.perez.core.service.qr.QrBitmapGenerator;
import daniel.perez.fileselectview.databinding.ActivityFileSelectBinding;
import daniel.perez.fileselectview.di.FileSelectComponentProvider;
import io.reactivex.disposables.CompositeDisposable;

public class FileSelectActivity extends AppCompatActivity
{
    @Inject FileManager fileManager;
    @Inject QrBitmapGenerator generator;
    private CompositeDisposable disposables = new CompositeDisposable();
    private CardsAdapter adapter;
    private Uri fileUri;
    private Intent resultIntent;
    private ActivityFileSelectBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState)
    {
        ((FileSelectComponentProvider) getApplication())
                .provideFileSelectComponent()
                .inject(this);
        super.onCreate(savedInstanceState, persistentState);
        binding = ActivityFileSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        resultIntent = new Intent("projects.csce.evence.ACTION_RETURN_FILE");
        setResult(AppCompatActivity.RESULT_CANCELED, null);

        adapter = new CardsAdapter(this);
        binding.fileSelector.setAdapter(adapter);


        disposables.add(adapter.clicks().subscribe(ical ->
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
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}
