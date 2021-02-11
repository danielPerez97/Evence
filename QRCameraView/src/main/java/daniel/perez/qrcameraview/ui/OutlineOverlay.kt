package daniel.perez.qrcameraview.ui

import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.ImageView
import daniel.perez.qrcameraview.R
import daniel.perez.qrcameraview.databinding.ActivityQrReaderBinding

class OutlineOverlay constructor(internal val context: Context, private val binding: ActivityQrReaderBinding,) : View(context) {
    lateinit var paint: Paint
    lateinit var focusedPaint: Paint
    var overlays : MutableList<RectF> = mutableListOf()
    var ivs: MutableList<ImageView> = mutableListOf()

    private lateinit var  labelPaint: Paint

    init{ setup() }

    fun setup() {
        paint = Paint()
        paint.color = Color.WHITE
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE

        focusedPaint = Paint()
        focusedPaint.color = Color.WHITE
        focusedPaint.strokeWidth = 30f
        focusedPaint.style = Paint.Style.STROKE
        focusedPaint.setAntiAlias(true);
        focusedPaint.setShadowLayer(12f, 3f, 3f, Color.BLACK);


        labelPaint = Paint()
        labelPaint.style = Paint.Style.FILL
        labelPaint.textSize=70f
    }

    fun addOverlay(rects: List<Rect?>) {
        overlays = mutableListOf()
        for (i in rects.indices) {
            val newRectF = RectF(rects[i])
            overlays.add(newRectF)
            var iv = ImageView(context)
            iv.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_qr_code_scanner_24))
            ivs.add(iv)
            ivs[i].x = overlays[i].centerX()
            ivs[i].y = overlays[i].centerY()
            binding.overlayLayout.addView(ivs[i])
        }

        invalidate()
    }

    fun clearOverlays(){
        overlays.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        binding.overlayLayout.invalidate()
        binding.overlayLayout.removeAllViews()
        for (i in overlays.indices){

            if (i == 0)
                canvas.drawRoundRect(overlays[i],35f,35f, focusedPaint)
            canvas.drawRoundRect(overlays[i],35f,35f, paint)



            if (i > 4) //only shows 6 qr codes at a time for more stability
                break
        }
    }


}