package daniel.perez.qrcameraview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View

class BarcodeOverlay(context: Context) : View(context) {
    lateinit var paint: Paint
    lateinit var rect : Rect

    init{
        setup()
    }

    fun setup() {
        paint = Paint()
        paint.color = Color.WHITE
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        rect = Rect()
    }

    fun updateRect(rectangle: Rect) {
//        rect.set(convertDPtoPX(context, rectangle.left.toFloat()),
//                convertDPtoPX(context, rectangle.top.toFloat()),
//                convertDPtoPX(context, rectangle.right.toFloat()),
//                convertDPtoPX(context, rectangle.bottom.toFloat()))

        rect.set(rectangle.left,
             rectangle.top,
            rectangle.right,
            rectangle.bottom)

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(rect, paint)
    }



}