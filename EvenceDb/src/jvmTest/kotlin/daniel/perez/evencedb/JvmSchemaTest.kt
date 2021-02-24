package daniel.perez.evencedb

import daniel.perez.evencedb.data.after
import daniel.perez.evencedb.data.before
import kotlinx.datetime.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class JvmSchemaTest: BaseTest()
{
    lateinit var queries: EventQueries

    @BeforeTest
    fun setUpTestData()
    {
        queries = getDb().eventQueries
        queries.insertEvent(
                "Endgame Premiere",
                "Ending to MCU Phase 3",
                "Malco Theaters",
                Clock.System.now().toLocalDateTime(TimeZone.UTC),
                Clock.System.now().plus(5, DateTimeUnit.MINUTE).toLocalDateTime(TimeZone.UTC)
        )
        queries.insertEvent(
                "Wonder Woman 84",
                "A very bad movie",
                "HBO Max",
                Clock.System.now().plus(30, DateTimeUnit.HOUR).toLocalDateTime(TimeZone.UTC),
                Clock.System.now().plus(5, DateTimeUnit.MINUTE).toLocalDateTime(TimeZone.UTC)
        )
    }

    @Test
    fun eventsCreated()
    {
        // Test for the existence of  data in the database
        assertTrue { queries.selectAll().executeAsList().isNotEmpty() }
    }

    @Test
    fun testEventData()
    {
        val endGame = queries.selectByTitle("Endgame Premiere").executeAsOne()
        assertEquals("Endgame Premiere", endGame.title)
        assertEquals("Ending to MCU Phase 3", endGame.description)
        assertEquals("Malco Theaters", endGame.location)

        val wonderWoman = queries.selectByTitle("Wonder Woman 84").executeAsOne()
        assertEquals("Wonder Woman 84", wonderWoman.title)
        assertEquals("A very bad movie", wonderWoman.description)
        assertEquals("HBO Max", wonderWoman.location)
    }

    @Test
    fun eventsSortedSoonest()
    {
        val events: List<Event> = queries.getEventsSortedSoonest().executeAsList()

        events.zipWithNext { a, b ->
            assertTrue { a.start_time.before(b.end_time) }
        }

    }

    @Test
    fun eventsSortedLatest()
    {
        val events: List<Event> = queries.getEventsSortedLatest().executeAsList()

        events.zipWithNext { a, b ->
            assertTrue { a.start_time.after(b.end_time) }
        }

    }
}