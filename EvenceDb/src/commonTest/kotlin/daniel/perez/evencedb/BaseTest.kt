package daniel.perez.evencedb

import kotlin.test.AfterTest
import kotlin.test.BeforeTest

open class BaseTest {

    @BeforeTest
    fun initDb()
    {
        createDriver()
    }

    @AfterTest
    fun closeDb()
    {
        closeDriver()
    }
}


expect fun createDriver()

expect fun closeDriver()

expect fun BaseTest.getDb(): EvenceDatabase