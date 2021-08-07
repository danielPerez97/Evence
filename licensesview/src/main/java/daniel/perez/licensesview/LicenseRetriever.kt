package daniel.perez.licensesview

import daniel.perez.licensesview.data.License

interface LicenseRetriever
{
    suspend fun getLicenses(): List<License>
}