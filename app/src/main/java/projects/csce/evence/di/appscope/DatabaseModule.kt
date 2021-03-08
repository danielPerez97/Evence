package projects.csce.evence.di.appscope

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import daniel.perez.core.db.EventOps
import daniel.perez.core.service.FileManager
import daniel.perez.evencedb.EvenceDatabase
import daniel.perez.evencedb.data.EvenceSchema
import daniel.perez.evencedb.data.createQueryWrapper
import projects.csce.evence.database.getEventOps
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule
{
    @Provides
    @Singleton
    fun provideDatabase(
            @ApplicationContext appContext: Context,
            fileManager: FileManager
    ): EventOps
    {
        val driver = AndroidSqliteDriver(EvenceSchema, appContext, "evence.db")
        val db: EvenceDatabase = createQueryWrapper(driver)
        return appContext.getEventOps( db.eventQueries, fileManager )
    }
}