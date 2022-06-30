package teamevence.evenceapp.fileselectview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import coil.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import teamevence.evenceapp.core.BaseActivity
import teamevence.evenceapp.core.adapter.CardsAdapter
import teamevence.evenceapp.core.plusAssign
import teamevence.evenceapp.core.service.FileManager
import teamevence.evenceapp.core.service.qr.QrBitmapGenerator
import teamevence.evenceapp.fileselectview.databinding.ActivityFileSelectBinding
import teamevence.evenceapp.fileselectview.di.FileSelectComponentProvider
import javax.inject.Inject

@AndroidEntryPoint
class FileSelectActivity : BaseActivity() {
    @Inject lateinit var fileManager: FileManager
    @Inject lateinit var generator: QrBitmapGenerator
    @Inject lateinit var imageLoader: ImageLoader
    private lateinit var cardsAdapter: CardsAdapter
    private var fileUri: Uri? = null
    private var resultIntent: Intent? = null
    private var binding: ActivityFileSelectBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as FileSelectComponentProvider)
                .provideFileSelectComponent()
                .inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityFileSelectBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        resultIntent = Intent("projects.csce.evence.ACTION_RETURN_FILE")
        setResult(RESULT_CANCELED, null)
        cardsAdapter = CardsAdapter()
        binding!!.fileSelector.adapter = cardsAdapter

        disposables += cardsAdapter.clicks()
                .subscribe { viewEvent ->
                    fileUri = fileManager.getContentUri( viewEvent.icsUri.toString() )
                    if (fileUri != null)
                    {
                        // Grant temporary read permission
                        resultIntent!!.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        // Pur the Uri and MIME type in the result Intent
                        resultIntent!!.setDataAndType(fileUri, contentResolver.getType(fileUri!!))

                        // Set the result
                        setResult(RESULT_OK, resultIntent)
                    }
                    else
                    {
                        resultIntent!!.setDataAndType(null, "")
                        setResult(RESULT_CANCELED, resultIntent)
                    }
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}