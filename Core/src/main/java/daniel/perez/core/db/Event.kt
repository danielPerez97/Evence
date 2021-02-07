package daniel.perez.core.db

import java.time.LocalDateTime

data class Event(
        val id: Long,
        val title: String,
        val description: String,
        val location: String,
        val startTime: LocalDateTime,
        val endTime: LocalDateTime,
        val recurrenceRule: String? = null
)