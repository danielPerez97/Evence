package daniel.perez.fileselectview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import coil.ImageLoader
import daniel.perez.core.BaseActivity
import daniel.perez.core.adapter.CardsAdapter
import daniel.perez.core.service.FileManager
import daniel.perez.core.service.qr.QrBitmapGenerator
import daniel.perez.fileselectview.databinding.ActivityFileSelectBinding
import daniel.perez.fileselectview.di.FileSelectComponentProvider
import javax.inject.Inject

class FileSelectActivity : BaseActivity() {
    @Inject lateinit var fileManager: FileManager
    @Inject lateinit var generator: QrBitmapGenerator
    @Inject lateinit var imageLoader: ImageLoader
    private lateinit var adapter: CardsAdapter
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
        adapter = CardsAdapter(this, imageLoader)
        binding!!.fileSelector.adapter = adapter

//        disposables.add(adapter.clicks().subscribe { (fileName) ->
//            fileUri = fileManager.getFileUri(fileName)
//            if (fileUri != null) {
//                // Grant temporary read permission
//                resultIntent!!.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//
//                // Pur the Uri and MIME type in the result Intent
//                resultIntent!!.setDataAndType(fileUri, contentResolver.getType(fileUri!!))
//
//                // Set the result
//                setResult(RESULT_OK, resultIntent)
//            } else {
//                resultIntent!!.setDataAndType(null, "")
//                setResult(RESULT_CANCELED, resultIntent)
//            }
//        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}