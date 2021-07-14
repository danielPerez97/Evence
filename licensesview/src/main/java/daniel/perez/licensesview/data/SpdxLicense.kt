package daniel.perez.licensesview.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpdxLicense(
    val identifier: String,
    val name: String,
    val url: String
)