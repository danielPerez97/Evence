package teamevence.evenceapp.core.db

import java.time.LocalDateTime

data class UiNewEvent(
        val title: String,
        val description: String,
        val location: String,
        val start: LocalDateTime,
        val end: LocalDateTime
)
