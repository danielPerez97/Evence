package daniel.perez.qrcameraview.ui

import android.content.Context
import android.graphics.Canvas
import android.view.View
import com.google.mlkit.vision.barcode.Barcode
import daniel.perez.qrcameraview.data.ScannedData

class Overlays(context: Context?) : View(context) {
    val overlays : MutableList<BaseOverlay> = mutableListOf()

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val scaleX = width/720f
        val scaleY = height/1280f


        for(overlay in overlays) {
            overlay.setScale(minOf(scaleX, scaleY))
            overlay.draw(canvas)
        }
    }
}