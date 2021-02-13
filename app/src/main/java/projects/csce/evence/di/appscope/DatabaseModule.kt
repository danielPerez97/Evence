package projects.csce.evence.di.appscope

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import daniel.perez.evencedb.EvenceDatabase
import daniel.perez.evencedb.data.EvenceSchema
import daniel.perez.evencedb.data.createQueryWrapper
import daniel.perez.core.db.EventOps
import daniel.perez.core.service.FileManager
import projects.csce.evence.database.getEventOps

@Module
class DatabaseModule(appContext: Context)
{
    private val db: EvenceDatabase
    init {
        val driver = AndroidSqliteDriver(EvenceSchema, appContext, "evence.db")
        db = createQueryWrapper(driver)
    }

    @Provides
    fun provideDatabase(fileManager: FileManager): EventOps
    {
        return getEventOps( db.eventQueries, fileManager )
    }
}