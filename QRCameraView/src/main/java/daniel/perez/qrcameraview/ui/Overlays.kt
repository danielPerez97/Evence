package daniel.perez.qrcameraview.ui

import android.content.Context
import android.graphics.Canvas
import android.util.Size
import android.view.View
import com.google.mlkit.vision.barcode.Barcode
import daniel.perez.qrcameraview.data.ScannedData
import timber.log.Timber

class Overlays(context: Context?) : View(context) {
    private val overlays : MutableList<BaseOverlay> = mutableListOf()
    private var scannedImageSize = Size(0,0) //fix, find better init

    fun updateOverlays(scannedData: List<ScannedData>){
        clearOverlays()
        for (item in scannedData){
            val overlay = RectOverlay(context, item.data as Barcode) //todo fix abstraction
            overlays.add(overlay)

        }
        postInvalidate()
    }

    fun clearOverlays(){
        overlays.clear()
        postInvalidate()
    }

    fun updateScannedImageSize(size: Size){
        scannedImageSize = size
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val scaleX = width/scannedImageSize.width.toFloat()
        val scaleY = height/scannedImageSize.height.toFloat()

        Timber.i("imagewidth = " + scannedImageSize.width.toFloat() +
                "imageHeight = " + scannedImageSize.height.toFloat()
        )

        for(overlay in overlays) {
            overlay.setScale(minOf(scaleX, scaleY))
            overlay.draw(canvas)
        }
    }
}