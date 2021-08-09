package daniel.perez.ical

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestEventSpec
{
    lateinit var christmas1997Event: ZonedDateTime

    @BeforeAll
    fun setup()
    {
        christmas1997Event = ZonedDateTime.of(
                1997,
                Month.DECEMBER.value,
                25,
                6,
                0,
                0,
                0,
                ZoneId.of("America/Chicago")
        )
    }

    @Test
    fun `tests a two hour event on christmas 1997`()
    {
        val dtstamp = ZonedDateTime.of(
                1997,
                Month.DECEMBER.value,
                25,
                5,
                59,
                0,
                0,
                ZoneId.of("America/Chicago")
        )

        // Creat
        val eventSpec = EventSpec.Builder(2)
                .start(christmas1997Event)
                .end(christmas1997Event.plusHours(2))
                .dtstamp(dtstamp)
                .title("Christmas 1997")
                .build()

        assertEquals(TestStrings.christmas1997, eventSpec.text())
    }

    @Test
    fun `test that a unique dtstamp is generated if not explicitly set`()
    {
        val eventone = EventSpec.Builder(2)
            .start(christmas1997Event)
            .end(christmas1997Event.plusHours(2))
            .title("Christmas 1997")
            .build()

        // Wait 100 milliseconds for system time to change
        Thread.sleep(100)

        val eventTwo = EventSpec.Builder(2)
                .start(christmas1997Event)
                .end(christmas1997Event.plusHours(2))
                .title("Christmas 1997")
                .build()

        println(eventone.dtstamp.toString())
        println(eventTwo.dtstamp.toString())

        assertTrue(
                eventone.dtstamp.toString() != eventTwo.dtstamp.toString()
        )
    }
}