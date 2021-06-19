package daniel.perez.licensesview

import kotlinx.coroutines.flow.Flow

interface LicensesData
{
    fun getLicenses(): Flow<List<License>>
}