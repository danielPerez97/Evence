package projects.evenceteam.evence.di.appscope

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import teamevence.evenceapp.core.db.EventOps
import teamevence.evenceapp.core.service.FileManager
import teamevence.evenceapp.evencedb.EvenceDatabase
import teamevence.evenceapp.evencedb.data.EvenceSchema
import teamevence.evenceapp.evencedb.data.createQueryWrapper
import projects.evenceteam.evence.database.getEventOps
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