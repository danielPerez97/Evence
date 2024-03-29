package teamevence.evenceapp.licensesview

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import teamevence.evenceapp.licensesview.data.License
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class LicensesViewModel @Inject constructor(): ViewModel()
{
    fun licenses(retriever: LicenseRetriever): Flow<List<License>>
    {
        return flow {
            emit(retriever.getLicenses())
        }
    }

}