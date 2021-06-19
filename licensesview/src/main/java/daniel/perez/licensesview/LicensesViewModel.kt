package daniel.perez.licensesview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LicensesViewModel: ViewModel()
{
    fun licenses(): Flow<List<License>>
    {
        return flow {
            emit(listOf(License("AppCompat", "GPL") ))
        }
    }
}