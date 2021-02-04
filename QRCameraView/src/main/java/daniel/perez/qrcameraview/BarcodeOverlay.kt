package daniel.perez.qrcameraview

import android.content.Context
import android.graphics.*
import android.view.View

class BarcodeOverlay(context: Context) : View(context) {
    lateinit var paint: Paint
    lateinit var rect : RectF

    init{
        setup()
    }

    fun setup() {
        paint = Paint()
        paint.color = Color.WHITE
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        rect = RectF()
    }

    fun updateRect(rectangle: Rect) {
        rect.set(rectangle.left.toFloat(),
             rectangle.top.toFloat(),
            rectangle.right.toFloat(),
            rectangle.bottom.toFloat())


        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(rect,35f,35f, paint)
    }



}