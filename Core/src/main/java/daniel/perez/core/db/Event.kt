package daniel.perez.core.db

import android.net.Uri
import daniel.perez.ical.EventSpec
import daniel.perez.ical.ICalSpec
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

data class Event(
        val id: Long,
        val title: String,
        val description: String,
        val location: String,
        val startTime: LocalDateTime,
        val endTime: LocalDateTime,
        val qrImageFileUri: Uri,
        val qrImageContentUri: Uri,
        val icsFileUri: Uri,
        val recurrenceRule: String? = null
)
{
    fun icsText(zonedId: ZoneId = TimeZone.getDefault().toZoneId()): String
    {
        return ICalSpec.Builder()
                .addEvent(
                        EventSpec.Builder( this.id.toInt() )
                                .title( this.title )
                                .description( this.description )
                                .location( this.location )
                                .start( this.startTime.atZone( zonedId ) )
                                .end( this.endTime.atZone( zonedId ) )
                                .build()
                )
                .build()
                .text()
    }
}