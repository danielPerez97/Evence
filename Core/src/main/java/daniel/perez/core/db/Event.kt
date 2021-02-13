package daniel.perez.core.db

import android.net.Uri
import java.time.LocalDateTime

data class Event(
        val id: Long,
        val title: String,
        val description: String,
        val location: String,
        val startTime: LocalDateTime,
        val endTime: LocalDateTime,
        val qrImageUri: Uri,
        val recurrenceRule: String? = null
)