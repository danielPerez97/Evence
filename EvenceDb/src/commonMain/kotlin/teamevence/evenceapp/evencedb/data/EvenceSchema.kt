package teamevence.evenceapp.evencedb.data

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import teamevence.evenceapp.evencedb.EvenceDatabase
import teamevence.evenceapp.evencedb.Event
import kotlinx.datetime.LocalDateTime

fun createQueryWrapper(driver: SqlDriver): EvenceDatabase
{
    return EvenceDatabase(
            driver = driver,
            EventAdapter = Event.Adapter(
                    EvenceSchema.LocalDateTimeAdapter,
                    EvenceSchema.LocalDateTimeAdapter
            )
    )
}

object EvenceSchema: SqlDriver.Schema by EvenceDatabase.Schema
{
    override fun create(driver: SqlDriver) {
        // Create the database
        EvenceDatabase.Schema.create(driver)
        createQueryWrapper(driver).apply {

        }
    }

    /**
     * Sqlite supports operations on ISO8601 compliant Strings. Kotlinx-datetime's
     * types support this strings with their toString() methods. As long as the database
     * is getting ISO8601 strings, we can leverage this in our SQL code.
     */
    object LocalDateTimeAdapter: ColumnAdapter<LocalDateTime, String>
    {
        override fun decode(databaseValue: String): LocalDateTime
        {
            return LocalDateTime.parse(databaseValue)
        }

        override fun encode(value: LocalDateTime): String
        {
            return value.toString()
        }

    }
}