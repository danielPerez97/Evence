package daniel.perez.licensesview.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class License(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val spdxLicenses: List<SpdxLicense>? = null,
    val unknownLicenses: List<UnknownLicense>? = null
)
