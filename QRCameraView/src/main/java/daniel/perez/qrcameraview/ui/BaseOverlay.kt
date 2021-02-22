package daniel.perez.qrcameraview.ui

import android.content.Context
import android.graphics.Canvas

abstract class BaseOverlay(context: Context?)  {
    abstract fun draw(canvas: Canvas)
    abstract fun setScale(scale: Float)
}