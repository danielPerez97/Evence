package teamevence.evenceapp.qrcameraview

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.google.mlkit.vision.barcode.Barcode

class IntentActions(private val context: Context) {

    fun performIntent(intent: Intent) {
        Log.d("IntentActions-------","performintent1")
        if (intent.resolveActivity(context.packageManager) != null) {
            Log.d("IntentActions-------","performintent2")
            context.startActivity(intent)
        }
    }

    fun openWebpage( url: Barcode.UrlBookmark) {
        val webpage: Uri = Uri.parse(url.url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        performIntent(intent)
    }

    fun saveContact( contactInfo: Barcode.ContactInfo) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            type = ContactsContract.Contacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.NAME, contactInfo.name?.formattedName)
            putExtra(ContactsContract.Intents.Insert.EMAIL, contactInfo.emails[0]?.address)
            putExtra(ContactsContract.Intents.Insert.PHONE, contactInfo.phones[0]?.number)
            putExtra(ContactsContract.Intents.Insert.COMPANY, contactInfo.organization?.toString())
        }
        performIntent(intent)
    }

    fun sendEmail( emailData: Barcode.Email) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, listOf(emailData.address).toTypedArray())
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.body)
        }
        performIntent(intent)
    }

    fun performCall( phone : Barcode.Phone) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:" + phone.number)
        }
        performIntent(intent)
    }

    fun sendSMS(sms: Barcode.Sms) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto: " + sms.phoneNumber)  // This ensures only SMS apps respond
            putExtra("sms_body", sms.message)
            putExtra("exit_on_sent", true)
        }
        performIntent(intent)
    }

    fun searchWeb(qrValue : Barcode){
        val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
            putExtra(SearchManager.QUERY, qrValue.displayValue)
        }
        performIntent(intent)
    }

    fun searchGeo(geoPoint : Barcode.GeoPoint){
        val latlng = Uri.parse("geo: ${geoPoint.lat}, ${geoPoint.lng}" )
        val intent = Intent(Intent.ACTION_VIEW, latlng)
        performIntent(intent)
    }
}