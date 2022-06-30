package teamevence.evenceapp.core.db

import teamevence.evenceapp.core.model.ViewEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.dateString(): String
{
    return this.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
}

fun LocalDateTime.timeString(): String
{
    return this.format(DateTimeFormatter.ofPattern("hh:mm a"))
}

fun Event.toViewEvent(): ViewEvent
{
    return ViewEvent(
            this.id,
            this.title,
            this.description,
            this.location,
            this.startTime,
            this.endTime,
            this.qrImageFileUri,
            this.qrImageContentUri,
            this.icsFileUri
    )
}