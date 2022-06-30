package teamevence.evenceapp.evencedb.data

import com.squareup.sqldelight.db.SqlDriver
import teamevence.evenceapp.evencedb.EvenceDatabase

object Db
{
    private var driverRef: SqlDriver? = null
    private var dbRef: EvenceDatabase? = null

    val ready: Boolean
        get() = driverRef != null

    fun dbSetup(driver: SqlDriver)
    {
        val db = createQueryWrapper(driver)
        driverRef = driver
        dbRef = db
    }

    internal fun dbClear() {
        driverRef!!.close()
        dbRef = null
        driverRef = null
    }

    val instance: EvenceDatabase
        get() = dbRef!!
}