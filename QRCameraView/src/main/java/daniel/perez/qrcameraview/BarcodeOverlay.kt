package daniel.perez.qrcameraview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import timber.log.Timber

class BarcodeOverlay(context: Context, private var rect: Rect) : View(context) {
    lateinit var paint: Paint
    init{
        setup()
    }

    fun setup() {
        paint = Paint()
        paint.color = Color.WHITE
        paint.strokeWidth = 20f
        paint.style = Paint.Style.STROKE

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        Timber.i("       " +
                rect.left.toFloat()+ " " +
                rect.top.toFloat() + " " +
                rect.right.toFloat() + " " +
                rect.bottom.toFloat() )
        canvas.drawRect(rect, paint)
    }



}