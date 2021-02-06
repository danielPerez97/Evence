package daniel.perez.qrcameraview.ui

import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.ImageView
import daniel.perez.qrcameraview.R

class Overlay(context: Context,) : View(context) {
    lateinit var paint: Paint
    var overlays : MutableList<RectF> = mutableListOf()

    init{
        setup()
    }

    fun setup() {
        paint = Paint()
        paint.color = Color.WHITE
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
    }

    fun addOverlay(rects: List<Rect?>) {
        overlays = mutableListOf()
        for (rect in rects) {
            val newRectF = RectF(rect)
            overlays.add(newRectF)
        }

        invalidate()
    }

    fun clearOverlays(){
        overlays.clear()
        invalidate()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val icon = ImageView(context)
        icon.setImageResource(R.drawable.ic_baseline_qr_code_scanner_24)
        for (rectf in overlays){
            canvas.drawRoundRect(rectf,35f,35f, paint)

        }

    }



}