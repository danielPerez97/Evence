package daniel.perez.evencedb

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import daniel.perez.evencedb.data.Db
import daniel.perez.evencedb.data.EvenceSchema

actual fun createDriver()
{
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    EvenceSchema.create(driver)
    Db.dbSetup(driver)
}

actual fun closeDriver()
{
    Db.dbClear()
}

actual fun BaseTest.getDb(): EvenceDatabase = Db.instance