package daniel.perez.core.model

import android.graphics.Bitmap

data class ViewEvent(
        val title: String,
        val description: String,
        val startDate: String,
        val startTime: String,
        val startInstantEpoch: Long,
        val endEpochMilli: Long,
        val location: String,
        val iCalText: String,
        val image: Bitmap
)