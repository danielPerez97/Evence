package teamevence.evenceapp.licensesview.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LicenseList(
    val licenses: List<License>
)