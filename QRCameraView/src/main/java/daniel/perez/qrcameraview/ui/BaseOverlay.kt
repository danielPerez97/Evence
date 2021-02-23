package daniel.perez.qrcameraview.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF

abstract class BaseOverlay(context: Context?)  {
    protected lateinit var boundingBox : RectF
    protected var scaleVal: Float = 0f

    abstract fun draw(canvas: Canvas)

    fun setScale(scale : Float) {
        scaleVal = scale
        scaleBoundingBox()
    }

    private fun scaleBoundingBox() {
        boundingBox.left *= scaleVal
        boundingBox.top *= scaleVal
        boundingBox.right *= scaleVal
        boundingBox.bottom *= scaleVal
    }
}