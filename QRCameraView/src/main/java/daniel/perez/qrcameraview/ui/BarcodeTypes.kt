package daniel.perez.qrcameraview.ui

import android.content.Context
import android.graphics.drawable.Drawable
import com.google.mlkit.vision.barcode.Barcode
import daniel.perez.core.copyToClipboard
import daniel.perez.qrcameraview.IntentActions
import daniel.perez.qrcameraview.R

class BarcodeTypes(val context: Context) {
    private val intentActions = IntentActions(context)
    fun getBarcodeTypeIcon(barcode: Barcode) : Drawable? {
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
            Barcode.TYPE_TEXT ->
                return context.getDrawable(R.drawable.ic_baseline_format_quote_24)
            else ->
                return context.getDrawable(R.drawable.ic_baseline_qr_code_scanner_24)
            //todo add isbn
        }
    }

    fun getBarcodeTypeString(barcode: Barcode) : String {
        when (barcode.valueType) {
            Barcode.TYPE_CALENDAR_EVENT ->
                return context.getString(R.string.calendar_event)
            Barcode.TYPE_URL ->
                return context.getString(R.string.url)
            Barcode.TYPE_CONTACT_INFO ->
                return context.getString(R.string.contact_info)
            Barcode.TYPE_EMAIL ->
                return context.getString(R.string.email)
            Barcode.TYPE_PHONE ->
                return context.getString(R.string.phone)
            Barcode.TYPE_SMS ->
                return context.getString(R.string.sms)
            Barcode.TYPE_ISBN ->
                return context.getString(R.string.isbn)
            Barcode.TYPE_WIFI ->
                return context.getString(R.string.wifi)
            Barcode.TYPE_GEO ->
                return context.getString(R.string.geo)
            Barcode.TYPE_DRIVER_LICENSE ->
                return context.getString(R.string.license)
            Barcode.TYPE_TEXT ->
                return context.getString(R.string.text)
            else ->
                return context.getString(R.string.unknown)
        }
    }

    fun performAction(barcode: Barcode){
        when (barcode.valueType) {
                Barcode.TYPE_URL -> intentActions.openWebpage(barcode.url)
                Barcode.TYPE_CONTACT_INFO -> intentActions.saveContact(barcode.contactInfo)
                Barcode.TYPE_EMAIL -> intentActions.sendEmail(barcode.email)
                Barcode.TYPE_PHONE -> intentActions.performCall(barcode.phone)
                Barcode.TYPE_SMS -> intentActions.sendSMS(barcode.sms)
                Barcode.TYPE_ISBN -> intentActions.searchWeb(barcode)
                Barcode.TYPE_WIFI -> copyToClipboard(context, "wifi password", barcode.wifi.password)
                Barcode.TYPE_GEO -> intentActions.searchGeo(barcode.geoPoint)
                else -> copyToClipboard(context, "Copy text", barcode.displayValue)
            }
    }
}