package teamevence.evenceapp.licensesview.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Scm(
        val url: String
)
