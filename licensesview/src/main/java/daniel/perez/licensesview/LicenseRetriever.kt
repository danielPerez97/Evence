package daniel.perez.licensesview

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import daniel.perez.licensesview.data.License
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import java.lang.reflect.Type

class LicenseRetriever(
    val moshi: Moshi, val context: Context
    )
{
    suspend fun getLicenses(): List<License>
    {
        return withContext(Dispatchers.IO) {
            val fileInputStream = context.resources.openRawResource(R.raw.artifacts)
            val jsonString = fileInputStream.source().buffer().readUtf8()

            val type: Type = Types.newParameterizedType(List::class.java, License::class.java)
            val adapter: JsonAdapter<List<License>> = moshi.adapter(type)
            return@withContext adapter.fromJson(jsonString)!!
        }
    }
}