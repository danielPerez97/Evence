package daniel.perez.qrcameraview.ui

import android.content.Context
import android.graphics.Canvas
import android.util.Size
import android.view.View
import daniel.perez.qrcameraview.data.ScannedData
import daniel.perez.qrcameraview.data.ScannedQR

class Overlays(context: Context?) : View(context) {
    private val overlays : MutableList<BaseOverlay> = mutableListOf()
    private var scannedImageSize = Size(0,0) //fix, find better init

    fun updateOverlays(scannedQR: List<ScannedData>){
        clearOverlays()
        for (item in scannedQR ){
            val overlay = RectOverlay(context, (item as ScannedQR).data)
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

        for(overlay in overlays) {
            overlay.setScale(minOf(scaleX, scaleY))
            overlay.draw(canvas)
        }
    }
}