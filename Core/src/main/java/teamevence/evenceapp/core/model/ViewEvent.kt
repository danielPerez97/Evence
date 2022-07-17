package teamevence.evenceapp.core.model

import android.net.Uri
import teamevence.evenceapp.core.db.timeString
import java.time.Duration
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.*

data class ViewEvent(
        val id: Long,
        val title: String,
        val description: String,
        val location: String,
        val startDateTime: LocalDateTime,
        val endDateTime: LocalDateTime,
        val imageFileUri: Uri,
        val imageFileContentUri: Uri,
        val icsUri: Uri
)
{

    fun startDatePretty(): String
    {
        return startDateTime.pretty()
    }

    fun startTimePretty(): String
    {
        return startDateTime.timeString()
    }

    fun endDatePretty(): String
    {
        return endDateTime.pretty()
    }

    fun endTimePretty(): String
    {
        return endDateTime.timeString()
    }

    fun startEpochMilli(): Long
    {
        return startDateTime.atZone( ZoneId.systemDefault() ).toInstant().toEpochMilli()
    }

    fun endEpochMilli(): Long
    {
        return endDateTime.atZone( ZoneId.systemDefault() ).toInstant().toEpochMilli()
    }

    private fun LocalDateTime.pretty(): String
    {
        val month = this.month.toString()
        return "${month.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)} ${this.dayOfMonth}, ${this.year}"
    }
}