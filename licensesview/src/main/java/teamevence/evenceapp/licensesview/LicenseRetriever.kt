package teamevence.evenceapp.licensesview

import teamevence.evenceapp.licensesview.data.License

interface LicenseRetriever
{
    suspend fun getLicenses(): List<License>
}