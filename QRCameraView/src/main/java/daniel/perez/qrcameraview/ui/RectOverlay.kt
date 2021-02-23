package daniel.perez.qrcameraview.ui

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import com.google.mlkit.vision.barcode.Barcode
import daniel.perez.qrcameraview.R

class RectOverlay constructor(internal val context: Context, private val barcode: Barcode ) : BaseOverlay(context) {
    private lateinit var outlinePaint: Paint
    private lateinit var focusedOutlinePaint: Paint
    private lateinit var bgPaint: Paint
    private lateinit var imgPaint: Paint
    private lateinit var labelPaint: Paint
    private lateinit var img : Bitmap

    init{
        initialize()
        setupPaint()
    }

    private fun initialize(){
        boundingBox = RectF(barcode.boundingBox)
        img = setBarcodeTypeIcon(barcode)!!.toBitmap()
    }

    private fun setupPaint() {
        outlinePaint = Paint()
        outlinePaint.color = Color.WHITE
        outlinePaint.strokeWidth = 8f
        outlinePaint.style = Paint.Style.STROKE

        focusedOutlinePaint = Paint()
        focusedOutlinePaint.color = context.getColor(R.color.blue1)
        focusedOutlinePaint.strokeWidth = 15f
        focusedOutlinePaint.style = Paint.Style.STROKE
        focusedOutlinePaint.isAntiAlias = true;
        focusedOutlinePaint.setShadowLayer(45f, 3f, 3f, Color.BLACK);

        bgPaint = Paint()
        bgPaint.color = Color.WHITE
        bgPaint.style = Paint.Style.FILL
        bgPaint.alpha = 150
        //backPaint.maskFilter = BlurMaskFilter(150f ,BlurMaskFilter.Blur.NORMAL)
        bgPaint.setShadowLayer(65f, 3f, 3f, Color.BLACK);

        imgPaint = Paint()
        imgPaint.style = Paint.Style.FILL
        //imgPaint.alpha = 110
        imgPaint.colorFilter = PorterDuffColorFilter(Color.WHITE,
                PorterDuff.Mode.SRC_IN)

        labelPaint = Paint()
        labelPaint.style = Paint.Style.FILL
        labelPaint.textSize=70f
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRoundRect(boundingBox, 35f, 35f, bgPaint)
        canvas.drawBitmap(img,
                    boundingBox.centerX() - (img.width / 2) ,
                    boundingBox.centerY() - (img.height / 2),
                    imgPaint)
        canvas.drawRoundRect(boundingBox,35f,35f, outlinePaint)
        //canvas.drawRoundRect(boundingBox, 35f, 35f, focusedOutlinePaint)
    }

    fun setBarcodeTypeIcon(barcode: Barcode) : Drawable? {
        when (barcode.valueType) {
            Barcode.TYPE_CALENDAR_EVENT ->
                return context.getDrawable(R.drawable.ic_event_white_36dp)
            Barcode.TYPE_URL ->
                return context.getDrawable(R.drawable.ic_open_in_new_white_24dp)
            Barcode.TYPE_CONTACT_INFO ->
                return context.getDrawable(R.drawable.ic_person_add_white_24dp)
            Barcode.TYPE_EMAIL ->
                return context.getDrawable(R.drawable.ic_email_white_24dp)
            Barcode.TYPE_PHONE ->
                return context.getDrawable(R.drawable.ic_phone_white_24dp)
            Barcode.TYPE_SMS ->
                return context.getDrawable(R.drawable.ic_textsms_black_24dp)
            Barcode.TYPE_ISBN ->
                return context.getDrawable(R.drawable.ic_shopping_cart_white_24dp)
            Barcode.TYPE_WIFI ->
                return context.getDrawable(R.drawable.ic_wifi_white_24dp)
            Barcode.TYPE_GEO ->
                return context.getDrawable(R.drawable.ic_place_white_24dp)
            Barcode.TYPE_DRIVER_LICENSE ->
                return context.getDrawable(R.drawable.ic_account_box_white_24dp)
            else ->
                return context.getDrawable(R.drawable.ic_baseline_qr_code_scanner_24)
        }
    }
}