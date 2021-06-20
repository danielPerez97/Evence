package daniel.perez.licensesview.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnknownLicense(
    val name: String,
    val url: String
)